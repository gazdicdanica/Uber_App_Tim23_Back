insert into users(type, admin_username, password, name, last_name)
VALUES ('admin', 'admin', '123', 'John', 'Doe');

insert into users (type, active, address, blocked, profile_picture, name, last_name, telephone_number, email, password)
VALUES ('passenger', true, 'Dimitrija Avramovic 4', false, '123asdq', 'Danica', 'Gazdic', '0691852001', 'test@email.com', '$2a$04$Vbug2lwwJGrvUXTj6z7ff.97IzVBkrJ1XfApfGNl.Z695zqcnPYra');
insert into users (type, active, address, blocked, profile_picture, name, last_name, telephone_number, email, password)
VALUES ('passenger', false, 'Maksima Gorkog 55', false, '123qwerty', 'Milos', 'Obradovic', '12341234', 'test2@email.com', '$2a$04$Vbug2lwwJGrvUXTj6z7ff.97IzVBkrJ1XfApfGNl.Z695zqcnPYra');

insert into users (type, active, address, blocked, email, last_name, name, password, telephone_number, profile_picture)
VALUES ('driver', true, 'Adresa1', false, 'pp@gmail.com', 'Peric', 'Pera', '$2a$04$Vbug2lwwJGrvUXTj6z7ff.97IzVBkrJ1XfApfGNl.Z695zqcnPYra', '0641212', 'qwer');
insert into users (type, active, address, blocked, email, last_name, name, password, telephone_number, profile_picture)
VALUES ('driver', false, 'Adresa2', false, 'pr@gmail.com', 'Radovanovic', 'Pera', '$2a$04$Vbug2lwwJGrvUXTj6z7ff.97IzVBkrJ1XfApfGNl.Z695zqcnPYra', '06412', 'qwer');
insert into users (type, active, address, blocked, email, last_name, name, password, telephone_number, profile_picture)
VALUES ('driver', false, 'Adresa2', false, 'pa@gmail.com', 'Bro', 'Da', '$2a$04$Vbug2lwwJGrvUXTj6z7ff.97IzVBkrJ1XfApfGNl.Z695zqcnPYra', '062134412', 'qwer');

insert into role (name) VALUES ('ROLE_USER');
insert into role (name) VALUES ('ROLE_ADMIN');
insert into role (name) VALUES ('ROLE_DRIVER');

insert into user_role (user_id, role_id) VALUES (1, 2);
insert into user_role (user_id, role_id) VALUES (2, 1);
insert into user_role (user_id, role_id) VALUES (3, 1);
insert into user_role (user_id, role_id) VALUES (4, 3);
insert into user_role (user_id, role_id) VALUES (5, 3);
insert into user_role (user_id, role_id) VALUES (6, 3);

insert into working_hours (end_time, start_time, driver_id) VALUES ('2022-12-22T22:37:56.469083', '2022-12-22T17:37:56.469083', 3);

insert into locations (longitude, latitude, address) VALUES (13.54, 11.11, 'Dimitrija Avramovica 3');
insert into locations (longitude, latitude, address) VALUES (13.54, 11.11, 'NTP');

insert into users_favourite_locations (passenger_id, favourite_locations_id) VALUES (1, 1);

insert into documents (name, photo, driver_id) VALUES ('Vozacka', 'qwer123', 3);

insert into vehicle_type (price, type) VALUES (300, 1);

insert into vehicles (babies, pets, capacity, plates, vehicle_model, driver_id, vehicle_type_id) VALUES (true, true, 4, 'ns680hs', 'skoda fabia', 3, 1);

insert into rides (babies, end_time, panic, pets, ride_status, start_time, time_estimate, total_price, driver_id, vehicle_id)
VALUES (true, '2022-12-21T16:48:49.439927', false, false, 5, '2022-12-21T16:48:43.439927', 3, 350, 4, 1);
insert into rides (babies, end_time, panic, pets, ride_status, start_time, time_estimate, total_price, driver_id, vehicle_id)
VALUES (true, '2022-12-21T16:48:49.439927', true, false, 3, '2022-12-21T16:48:43.439927', 3, 350, 5, 1);
insert into rides (babies, end_time, panic, pets, ride_status, start_time, time_estimate, total_price, driver_id, vehicle_id)
VALUES (false, '2022-12-22T17:26:00.093044', false, false, 2, '2022-12-22T17:26:00.093044', 3, 0, 6, 1);

insert into ride_passengers (passenger_id, ride_id) VALUES (3, 1);
insert into ride_passengers (passenger_id, ride_id) VALUES (2, 2);
insert into ride_passengers (passenger_id, ride_id) VALUES (2, 3);

insert into messages (message, time_of_sending, message_type, receiver_id, ride_id, sender_id)
VALUES ('Pozdrav svima', '2022-12-21T16:48:49.439927', 2, 3, 1, 1);

insert into panic (reason, time, ride_id, user_id) VALUES ('Fatal crash', '2022-12-21T16:48:43.439927', 2, 1);

insert into rejections (reason, time, ride_id, user_id) VALUES ('Putnik se nije pojavio', '2022-12-22T17:26:00.093044', 3, 1);

insert into reviews (comment, grade, driver_id, passenger_id, ride_id) VALUES ('Svaka cst', 5, 3, 1, 1);

insert into routes (distance, departure, destination) VALUES (4.2, 1, 2);
insert into rides_locations (ride_id, locations_id) VALUES (2, 1);


insert into user_activations (creation_date_time, lifespan, user_id)
VALUES ('2022-12-22T17:37:56.469083', '2022-12-22T17:37:56.469083', 3);