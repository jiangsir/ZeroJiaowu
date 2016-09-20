UPDATE `electives` SET course1='初級日文ABCD' WHERE id>=1862 and (course1='初級日文A' OR course1='初級日文B' OR course1='初級日文C' OR course1='初級日文D');
UPDATE `electives` SET course2='初級日文ABCD' WHERE id>=1862 and (course2='初級日文A' OR course2='初級日文B' OR course2='初級日文C' OR course2='初級日文D');
UPDATE `electives` SET course3='初級日文ABCD' WHERE id>=1862 and (course3='初級日文A' OR course3='初級日文B' OR course3='初級日文C' OR course3='初級日文D');
UPDATE `electives` SET course4='初級日文ABCD' WHERE id>=1862 and (course4='初級日文A' OR course4='初級日文B' OR course4='初級日文C' OR course4='初級日文D');

UPDATE `electives` SET course1='初級西班牙語AB' WHERE id>=1862 and (course1='初級西班牙語A' OR course1='初級西班牙語B');
UPDATE `electives` SET course2='初級西班牙語AB' WHERE id>=1862 and (course2='初級西班牙語A' OR course2='初級西班牙語B');
UPDATE `electives` SET course3='初級西班牙語AB' WHERE id>=1862 and (course3='初級西班牙語A' OR course3='初級西班牙語B');
UPDATE `electives` SET course4='初級西班牙語AB' WHERE id>=1862 and (course4='初級西班牙語A' OR course4='初級西班牙語B');

DELETE FROM `users` WHERE userid>=1252 AND (comment='高一仁' OR comment='高一和');


＃SELECT * FROM electives WHERE (course1=course2 OR course1=course3 OR course1=course4 OR course2=course3 OR course2=course4 OR course3=course4) AND jobid=40;
DELETE FROM electives WHERE (course1=course2 OR course1=course3 OR course1=course4 OR course2=course3 OR course2=course4 OR course3=course4) AND jobid=40;

//==================================================================
ALTER TABLE  `users` ADD  `role` VARCHAR( 50 ) NOT NULL AFTER  `usergroup`;
UPDATE users SET role='DEBUGGER' WHERE usergroup='GroupAdmin'; 
UPDATE users SET role='USER' WHERE usergroup='GroupUser'; 
ALTER TABLE `users` DROP `usergroup`;
