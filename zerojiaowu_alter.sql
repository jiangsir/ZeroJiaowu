ALTER TABLE  `users` DROP  `sessionid`;

ALTER TABLE  `electives` ADD  `courseid1` INT NOT NULL AFTER  `course4`;
ALTER TABLE  `electives` ADD  `courseid2` INT NOT NULL AFTER  `courseid1`;
ALTER TABLE  `electives` ADD  `courseid3` INT NOT NULL AFTER  `courseid2`;
ALTER TABLE  `electives` ADD  `courseid4` INT NOT NULL AFTER  `courseid3`;
ALTER TABLE  `electives` ADD  `selectedid` INT NOT NULL AFTER  `courseid4`


UPDATE electives SET courseid1=(SELECT id FROM courses WHERE name=electives.course1 AND jobid=electives.jobid); 
UPDATE electives SET courseid2=(SELECT id FROM courses WHERE name=electives.course2 AND jobid=electives.jobid); 
UPDATE electives SET courseid3=(SELECT id FROM courses WHERE name=electives.course3 AND jobid=electives.jobid); 
UPDATE electives SET courseid4=(SELECT id FROM courses WHERE name=electives.course4 AND jobid=electives.jobid); 
UPDATE electives SET selectedid=(SELECT id FROM courses WHERE name=electives.selected AND jobid=electives.jobid); 

ALTER TABLE  `courses` DROP INDEX  `jobid`;

ALTER TABLE  `electives` CHANGE  `course1`  `course1` VARCHAR( 100 ) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT  '';
ALTER TABLE  `electives` CHANGE  `course2`  `course2` VARCHAR( 100 ) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT  '';
ALTER TABLE  `electives` CHANGE  `course3`  `course3` VARCHAR( 100 ) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT  '';
ALTER TABLE  `electives` CHANGE  `course4`  `course4` VARCHAR( 100 ) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT  '';
ALTER TABLE  `electives` CHANGE  `selected`  `selected` VARCHAR( 100 ) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT  '';
ALTER TABLE  `electives` CHANGE  `selected`  `selected` VARCHAR( 100 ) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT  ''；

