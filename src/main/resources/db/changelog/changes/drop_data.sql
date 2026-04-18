-- Manual data cleanup script. NOT included in Liquibase migrations.
-- Run this to wipe course data before re-applying data.sql.

DELETE FROM student_repository;
DELETE FROM student_progress;
DELETE FROM git_repository;
DELETE FROM message_log;
DELETE FROM message;
DELETE FROM material;
DELETE FROM section;
DELETE FROM topic;
DELETE FROM block;
DELETE FROM course;