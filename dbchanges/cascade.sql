ALTER TABLE `counselling`.`user_history` 
DROP FOREIGN KEY `FKd8f7md75uuycl5mgtl394nqoi`;
ALTER TABLE `counselling`.`user_history` 
ADD CONSTRAINT `FKd8f7md75uuycl5mgtl394nqoi`
  FOREIGN KEY (`user_id`)
  REFERENCES `counselling`.`users` (`id`)
  ON DELETE CASCADE
  ON UPDATE RESTRICT;


ALTER TABLE `counselling`.`user_role` 
DROP FOREIGN KEY `FKj345gk1bovqvfame88rcx7yyx`;
ALTER TABLE `counselling`.`user_role` 
ADD CONSTRAINT `FKj345gk1bovqvfame88rcx7yyx`
  FOREIGN KEY (`user_id`)
  REFERENCES `counselling`.`users` (`id`)
  ON DELETE CASCADE;


ALTER TABLE `counselling`.`user_config` 
DROP FOREIGN KEY `FKl8juqglanlq5r8qxkaiftldqh`;
ALTER TABLE `counselling`.`user_config` 
ADD CONSTRAINT `FKl8juqglanlq5r8qxkaiftldqh`
  FOREIGN KEY (`user_id`)
  REFERENCES `counselling`.`users` (`id`)
  ON DELETE CASCADE
  ON UPDATE RESTRICT;


ALTER TABLE `counselling`.`wish` 
DROP FOREIGN KEY `FKix6wk85sh6g57t6iqtwr36k4k`;
ALTER TABLE `counselling`.`wish` 
ADD CONSTRAINT `FKix6wk85sh6g57t6iqtwr36k4k`
  FOREIGN KEY (`targetuser_id`)
  REFERENCES `counselling`.`users` (`id`)
  ON DELETE CASCADE;


ALTER TABLE `counselling`.`wish` 
DROP FOREIGN KEY `FK9eyeaa4aahrulv06w4p3wbss`;
ALTER TABLE `counselling`.`wish` 
ADD CONSTRAINT `FK9eyeaa4aahrulv06w4p3wbss`
  FOREIGN KEY (`sourceuser_id`)
  REFERENCES `counselling`.`users` (`id`)
  ON DELETE CASCADE;
