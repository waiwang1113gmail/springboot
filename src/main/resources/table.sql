CREATE  TABLE users (
  username VARCHAR(45) NOT NULL ,
  password VARCHAR(45) NOT NULL ,
  enabled TINYINT NOT NULL DEFAULT 1 ,
  PRIMARY KEY (username));
  CREATE TABLE user_roles (
  user_role_id int(11) NOT NULL AUTO_INCREMENT,
  username varchar(45) NOT NULL,
  role varchar(45) NOT NULL,
  PRIMARY KEY (user_role_id),
  UNIQUE KEY uni_username_role (role,username),
  KEY fk_username_idx (username),
  CONSTRAINT fk_username FOREIGN KEY (username) REFERENCES users (username));
  INSERT INTO users(username,password,enabled)
VALUES ('weige','password', true);

INSERT INTO user_roles (username, role)
VALUES ('weige', 'ROLE_USER');
INSERT INTO user_roles (username, role)
VALUES ('weige', 'ROLE_ADMIN');

DELIMITER //
CREATE PROCEDURE `new_user` (username varchar(45), password varchar(45))
LANGUAGE SQL
DETERMINISTIC
SQL SECURITY DEFINER
begin
	INSERT INTO users(username,password,enabled) values (username,password,1);
	INSERT INTO user_roles(username,role) values (username,"ROLE_USER");
END//


CREATE USER 'waiwang1113'@'localhost' IDENTIFIED BY 'password';
GRANT ALL PRIVILEGES ON * . * TO 'waiwang1113'@'localhost';