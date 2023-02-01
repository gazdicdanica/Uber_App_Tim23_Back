insert into users(type, active, address, blocked, profile_picture, telephone_number, username, email, password, name, last_name)
VALUES ('admin', true, 'AdresaAdmina', false, 'piaegrhfvpiae4h4', '064123412', 'admin@email.com' ,'admin@email.com', '$2a$04$Vbug2lwwJGrvUXTj6z7ff.97IzVBkrJ1XfApfGNl.Z695zqcnPYra', 'John', 'Doe');

insert into users (type, active, address, blocked, profile_picture, name, username, last_name, telephone_number, email, password)
VALUES ('passenger', true, 'Dimitrija Avramovic 4', false, '123asdq', 'Danica','test@email.com', 'Gazdic', '0691852001', 'test@email.com', '$2a$04$Vbug2lwwJGrvUXTj6z7ff.97IzVBkrJ1XfApfGNl.Z695zqcnPYra');
insert into users (type, active, address, blocked, profile_picture, name, username, last_name, telephone_number, email, password)
VALUES ('passenger', true, 'Maksima Gorkog 55', false, '123qwerty', 'Milos', 'test2@email.com','Obradovic', '12341234', 'test2@email.com', '$2a$04$Vbug2lwwJGrvUXTj6z7ff.97IzVBkrJ1XfApfGNl.Z695zqcnPYra');

insert into users (type, active, address, blocked, email, last_name, name, username, password, telephone_number, profile_picture, online)
VALUES ('driver', true, 'Adresa1', false, 'pp@gmail.com', 'Peric', 'Pera','pp@gmail.com' ,'$2a$04$Vbug2lwwJGrvUXTj6z7ff.97IzVBkrJ1XfApfGNl.Z695zqcnPYra', '0641212', 'qwer', false);
insert into users (type, active, address, blocked, email, last_name, name, username ,password, telephone_number, profile_picture, online)
VALUES ('driver', true, 'Adresa2', false, 'pr@gmail.com', 'Radovanovic', 'Pera', 'pr@gmail.com' , '$2a$04$Vbug2lwwJGrvUXTj6z7ff.97IzVBkrJ1XfApfGNl.Z695zqcnPYra', '06412', 'qwer', false);
insert into users (type, active, address, blocked, email, last_name, name, username, password, telephone_number, profile_picture, online)
VALUES ('driver', true, 'Adresa2', false, 'pa@gmail.com', 'Bro', 'Da', 'pa@gmail.com', '$2a$04$Vbug2lwwJGrvUXTj6z7ff.97IzVBkrJ1XfApfGNl.Z695zqcnPYra', '062134412', 'qwer', false);
insert into users (type, active, address, blocked, email, last_name, name, username, password, telephone_number, profile_picture, online)
VALUES ('driver', true, 'Adresa2', false, 'isomidobradovicv3@gmail.com', 'Obradovic', 'Milos', 'isomidobradovicv3@gmail.com', '$2a$04$Vbug2lwwJGrvUXTj6z7ff.97IzVBkrJ1XfApfGNl.Z695zqcnPYra', '0649144773', 'qwer', true);


insert into role (name) VALUES ('ROLE_USER');
insert into role (name) VALUES ('ROLE_ADMIN');
insert into role (name) VALUES ('ROLE_DRIVER');

insert into user_role (user_id, role_id) VALUES (1, 2);
insert into user_role (user_id, role_id) VALUES (2, 1);
insert into user_role (user_id, role_id) VALUES (3, 1);
insert into user_role (user_id, role_id) VALUES (4, 3);
insert into user_role (user_id, role_id) VALUES (5, 3);
insert into user_role (user_id, role_id) VALUES (6, 3);
-- insert into user_role (user_id, role_id) VALUES (7, 3);

insert into working_hours (end_time, start_time, driver_id) VALUES ('2023-01-02T10:00:00.469083', '2023-01-02T19:00:00.469083', 4);
insert into working_hours (end_time, start_time, driver_id) VALUES ('2023-01-03T10:00:00.123411', '2023-01-03T18:11:11.123411', 5);
insert into working_hours (end_time, start_time, driver_id) VALUES ('2023-01-02T10:00:00.123411', '2023-01-02T11:11:11.123411', 6);

insert into locations (longitude, latitude, address) VALUES (19.835671,45.258664, 'Dimitrija Avramovica 3');
insert into locations (longitude, latitude, address) VALUES (19.847719,45.244913, 'NTP');
insert into locations (longitude, latitude, address) VALUES (19.854553,45.252264, 'Maksima Gorkog 57');
insert into locations (longitude, latitude, address) VALUES (19.842469,45.244482 ,'Promenada, Novi Sad');

insert into documents (name, photo, driver_id) VALUES ('Vehicle Registration', 'qwer123', 4);
insert into documents (name, photo, driver_id) VALUES ('ID Card', 'qwer123', 4);

insert into vehicle_type (price, type) VALUES (100, 0);
insert into vehicle_type (price, type) VALUES (200, 1);
insert into vehicle_type (price, type) VALUES (300, 2);

insert into vehicles (babies, pets, capacity, plates, vehicle_model, driver_id, vehicle_type_id, current_location) VALUES (true, true, 4, 'NS-001-AA', 'Škoda Fabia', 4, 2, 1);
insert into vehicles (babies, pets, capacity, plates, vehicle_model, driver_id, vehicle_type_id, current_location) VALUES (true, true, 3, 'NS-001-AB', 'Volkswagen golf', 5, 1, 2);
insert into vehicles (babies, pets, capacity, plates, vehicle_model, driver_id, vehicle_type_id, current_location) VALUES (true, true, 4, 'NS-001-AC', 'BMW 530i', 6, 1, 3);



insert into rides (babies, start_time, panic, pets, ride_status, time_estimate, total_price, driver_id, vehicle_id,  vehicle_type)
VALUES (true, '2023-01-11T17:43:49.439927', false, false, 4, 6, 350, 4, 1, 1);
insert into rides (babies, start_time, panic, pets, ride_status, time_estimate, total_price, driver_id, vehicle_id, vehicle_type)
VALUES (true, '2023-01-11T17:43:49.439927', true, false, 4, 2, 350, 5, 1, 2);
insert into rides (babies, start_time, panic, pets, ride_status, time_estimate, total_price, driver_id, vehicle_id, vehicle_type)
VALUES (true, '2023-01-11T18:00:49.439927', true, false, 4, 9, 350, 7, 1, 0);
insert into rides (babies, start_time, panic, pets, ride_status, time_estimate, total_price, driver_id, vehicle_id, vehicle_type)
VALUES (false,'2023-01-11T17:43:49.439927', false, false, 4, 3, 500, 6, 1, 1);

insert into ride_passengers (passenger_id, ride_id) VALUES (2, 1);
insert into ride_passengers (passenger_id, ride_id) VALUES (3, 1);

insert into ride_passengers (passenger_id, ride_id) VALUES (3, 3);

insert into messages (message, time_of_sending, message_type, receiver_id, ride_id, sender_id)
VALUES ('Pozdrav svima', '2022-12-21T16:48:49.439927', 2, 3, 1, 6);

insert into panic (reason, time, ride_id, user_id) VALUES ('Fatal crash', '2022-12-21T16:48:43.439927', 2, 2);

insert into rejections (reason, time, ride_id, user_id) VALUES ('Putnik se nije pojavio', '2022-12-22T17:26:00.093044', 2, 1);

insert into reviews (comment, grade, driver_id, passenger_id, ride_id, vehicle) VALUES ('Svaka cst', 5, 3, 2, 1, true);
insert into reviews (comment, grade, driver_id, passenger_id, ride_id, vehicle) VALUES ('Svaka ti', 5, 4, 3, 1, true);
insert into reviews (comment, grade, driver_id, passenger_id, ride_id, vehicle) VALUES ('Svaka bajo', 5, 5, 2, 1, false);
insert into reviews (comment, grade, driver_id, passenger_id, ride_id, vehicle) VALUES ('Svaka moj', 5, 4, 2, 1, false);


insert into routes (distance, departure, destination) VALUES (3.0, 1, 2);
insert into routes (distance, departure, destination) VALUES (2.1, 1, 3);
insert into routes (distance, departure, destination) VALUES (2.1, 2, 3);
insert into routes (distance, departure, destination) VALUES(1.6, 3, 4);

insert into rides_locations (ride_id, locations_id) VALUES (3, 1);
insert into rides_locations (ride_id, locations_id) VALUES (1, 2);
insert into rides_locations (ride_id, locations_id) VALUES (2, 3);
insert into rides_locations (ride_id, locations_id) VALUES (4, 4);

insert into notes (user_id, creation_date, message) VALUES (5, '2022-12-21T16:48:49.439927', 'Daj radi vise');

insert into user_activations (creation_date_time, lifespan, user_id)
VALUES ('2022-12-22T17:37:56.469083', '2022-12-22T17:37:56.469083', 3);