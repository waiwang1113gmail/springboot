package wanwe17.springboot;

import java.awt.image.BufferedImage;
import java.io.File;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import javax.imageio.ImageIO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


@Component
public class VideoManager {
	private String videoPath="src/main/resources/public/videos";
	private String framePath="src/main/resources/public/images";
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	
	
	private Map<String,Video> getAllVideoFromDatabase(){
		String SQL="SELECT * FROM videos";
		final Map<String,Video> map=new HashMap<>();
		jdbcTemplate.query(SQL, new Object[]{}, new RowMapper<Video>(){
			@Override
			public Video mapRow(ResultSet result, int index) throws SQLException {
				Video v=new Video();
				v.setFrame(result.getString("frame"));
				v.setName(result.getString("name"));
				map.put(v.getName(), v);
				return v;
			}
			
		});
		return map;
	}
	
	
	@Scheduled(fixedDelay = 100000)
	public void updateVideoData(){
		System.out.println("Starting task");
		insertVideoRecordToDatabase();
		
		System.out.println("Ending task");
	}


	private void insertVideoRecordToDatabase() {
		Map<String,Video> existingVideoRecordInDatabase=getAllVideoFromDatabase();
		Set<File> videoFilesInVideoFolder=getAllVideos();
		List<Video> newVideoRecord=new ArrayList<>();
		for(File videoFile:videoFilesInVideoFolder){
			if(!existingVideoRecordInDatabase.containsKey(videoFile.getName())){
				String frameName = fetchFrameFromVideoAndStoreItToFrameFolder(videoFile);
				if(frameName!=null){
					Video v=new Video();
					v.setFrame(frameName);
					v.setName(videoFile.getName());
					newVideoRecord.add(v);
				}
			}
		}
		addVideoRecordToDatabase(newVideoRecord);
	}

	private void addVideoRecordToDatabase(final List<Video> newVideoRecord) {
		String sql = "INSERT INTO videos " +
				"(name,frame) VALUES (?, ?)";
						
			  jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
						
				@Override
				public void setValues(PreparedStatement ps, int i) throws SQLException {
					Video v = newVideoRecord.get(i);
					ps.setString(1, v.getName());
					ps.setString(2, v.getFrame());
				}
						
				@Override
				public int getBatchSize() {
					return newVideoRecord.size();
				}
			  });
		
	}

	private Random rand=new Random();
	private String fetchFrameFromVideoAndStoreItToFrameFolder(File videoFile) {
		try {
			
			BufferedImage image=GrabFrameFromVideo.getFrame(videoFile, 12.0);
			String frameName=null;
			boolean finished=false;
			while(!finished){
				frameName=Long.toHexString(rand.nextLong())+".png";
				if(!new File(this.framePath,frameName).exists()){
					finished=true;
				}
			}
			ImageIO.write(image, "png",new File(this.framePath,frameName));
			return frameName;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private Set<File> getAllVideos(){
		System.out.println(new File(this.videoPath).getAbsolutePath());
		return arrayToSet(new File(this.videoPath).listFiles());
	}

	private <T> Set<T> arrayToSet(T [] d){
		Set<T> t=new HashSet<T>();
		t.addAll(Arrays.asList(d));
		return t;
	}
	private Set<File> getAllFrame() {
		System.out.println(new File(this.framePath).getAbsolutePath());
		return arrayToSet(new File(this.framePath).listFiles());
	}
}
