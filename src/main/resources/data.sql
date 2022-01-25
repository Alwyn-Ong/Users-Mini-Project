-- phpMyAdmin SQL Dump
-- version 5.1.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1:3306
-- Generation Time: Jan 24, 2022 at 04:03 PM
-- Server version: 5.7.36
-- PHP Version: 7.4.26

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `miniproject`
--
-- Dumping data for table `users`
--

INSERT INTO `users`
    SELECT t.*
    from (
        (SELECT 1 as user_id, 'Bob' as name, 2000.00 as salary)
        UNION ALL (SELECT 2, 'Carl', 3000.20)
        UNION ALL (SELECT 3, 'Dan', 4000.30)
        UNION ALL (SELECT 4, 'Alex', 3500.40)
        UNION ALL (SELECT 5, 'Emily', 2500.50)
        UNION ALL (SELECT 6, 'Fabian', 2920.00)
    ) t
    WHERE NOT EXISTS (SELECT * FROM `users`);


COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
