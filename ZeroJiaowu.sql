-- phpMyAdmin SQL Dump
-- version 3.4.11.1deb2
-- http://www.phpmyadmin.net
--
-- 主機: localhost
-- 產生日期: 2015 年 12 月 29 日 14:59
-- 伺服器版本: 5.5.31
-- PHP 版本: 5.4.4-14+deb7u4

SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- 資料庫: `zerojiaowu`
--

-- --------------------------------------------------------

--
-- 表的結構 `courses`
--

CREATE TABLE IF NOT EXISTS `courses` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `jobid` int(11) NOT NULL DEFAULT '0',
  `name` varchar(50) NOT NULL,
  `capacity` int(11) NOT NULL DEFAULT '30',
  `teacher` varchar(100) NOT NULL,
  `content` text NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `jobid` (`jobid`,`name`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- 表的結構 `electives`
--

CREATE TABLE IF NOT EXISTS `electives` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `jobid` int(11) NOT NULL DEFAULT '0',
  `account` varchar(50) NOT NULL,
  `course1` varchar(100) NOT NULL,
  `course2` varchar(100) NOT NULL,
  `course3` varchar(100) NOT NULL,
  `course4` varchar(100) NOT NULL,
  `selected` varchar(100) DEFAULT NULL,
  `nth` int(11) NOT NULL DEFAULT '0',
  `lock` int(11) NOT NULL DEFAULT '0',
  `submittime` datetime NOT NULL,
  `ipfrom` varchar(20) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `jobid` (`jobid`,`account`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- 表的結構 `exceptions`
--

CREATE TABLE IF NOT EXISTS `exceptions` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `uri` varchar(100) NOT NULL DEFAULT '',
  `account` varchar(20) NOT NULL DEFAULT '',
  `ipaddr` varchar(20) NOT NULL DEFAULT '',
  `exceptiontype` varchar(255) NOT NULL,
  `exception` text,
  `exceptiontime` datetime NOT NULL DEFAULT '2006-06-08 00:00:00',
  PRIMARY KEY (`id`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- 表的結構 `jobs`
--

CREATE TABLE IF NOT EXISTS `jobs` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `semester` int(11) NOT NULL,
  `title` varchar(200) NOT NULL,
  `content` text NOT NULL,
  `allowedusers` varchar(200) NOT NULL,
  `max_choose` int(11) NOT NULL,
  `visible` tinyint(1) NOT NULL,
  `starttime` datetime NOT NULL,
  `finishtime` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- 表的結構 `loginlogs`
--

CREATE TABLE IF NOT EXISTS `loginlogs` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `userid` varchar(12) NOT NULL DEFAULT '',
  `account` varchar(20) NOT NULL DEFAULT '',
  `ipaddr` varchar(15) NOT NULL,
  `message` varchar(100) NOT NULL DEFAULT 'Unknown',
  `logintime` datetime DEFAULT NULL,
  `logouttime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- 表的結構 `users`
--

CREATE TABLE IF NOT EXISTS `users` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `account` varchar(20) NOT NULL,
  `username` varchar(100) NOT NULL,
  `passwd` varchar(50) NOT NULL,
  `number` int(11) NOT NULL,
  `comment` varchar(255) NOT NULL DEFAULT '',
  `role` varchar(50) NOT NULL,
  `sessionid` varchar(50) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `NONCLUSTERED` (`account`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;


