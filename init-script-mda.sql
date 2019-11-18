CREATE SCHEMA  IF NOT EXISTS `myAppDB`;
USE `myAppDB`;

SET FOREIGN_KEY_CHECKS = 0;

--
-- Table structure for table `customers`
--

DROP TABLE IF EXISTS `customers`;

CREATE TABLE `customers` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `first_name` varchar(45) DEFAULT NULL,
  `last_name` varchar(45) DEFAULT NULL,
  `email` varchar(45) DEFAULT NULL,
  `phone_number` varchar(45) DEFAULT NULL,
  `street_adress` varchar(45) DEFAULT NULL,
  `city` varchar(45) DEFAULT NULL,
  `country` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;

--
-- Table structure for table `concerts`
--

DROP TABLE IF EXISTS `concerts`;

CREATE TABLE `concerts` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `title` varchar(45) DEFAULT NULL,
  `description` varchar(512) DEFAULT NULL,
  `media` varchar(45) DEFAULT NULL,
  `price` int(12),
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;

--
-- Table structure for table `orders`
--

DROP TABLE IF EXISTS `orders`;

CREATE TABLE `orders` (
  `id` int(20) NOT NULL AUTO_INCREMENT,
  `nfc_data` varchar(45) DEFAULT NULL,
  `valid` boolean DEFAULT FALSE,
  `customer_id` int(11) NOT NULL,
  `concert_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  FOREIGN KEY (`customer_id`) REFERENCES `customers` (`id`),
  FOREIGN KEY (`concert_id`) REFERENCES `concerts` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;

CREATE TABLE `users` (
  `username` varchar(45) NOT NULL,
  `password` varchar(45) NOT NULL,
  `role` varchar(45) NOT NULL, 
  `customer_id` int(11) DEFAULT NULL,
  `employee_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`username`),
  FOREIGN KEY (`customer_id`) REFERENCES `customers` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;



INSERT INTO `users`(username, password, role, customer_id, employee_id) VALUE('admin','admin','ADM', null, null);

INSERT INTO `concerts`(id, title, description, media, price) VALUE(1, "Metallica", "Concert with the greatest band, Metallica here in Ålesund!", "/media/metallica.jpg", 500);
INSERT INTO `concerts`(id, title, description, media, price) VALUE(2, "Aerosmith", "Concert with the greatest band, Aerosmith here in Ålesund!", "/media/aerosmith.jpg", 200);
INSERT INTO `concerts`(id, title, description, media, price) VALUE(3, "Green Day", "Concert with the greatest band, Green Day here in Ålesund!", "/media/green-day.jpg", 300);
INSERT INTO `concerts`(id, title, description, media, price) VALUE(4, "Sigrid", "Concert with the greatest band, Sgird here in Ålesund!", "/media/sigrid.jpg", 999);

SET FOREIGN_KEY_CHECKS = 1;
