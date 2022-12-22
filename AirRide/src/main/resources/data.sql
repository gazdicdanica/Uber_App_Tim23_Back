insert into passengers (id, active, address, blocked, profile_photo, name, last_name, phone_number, email, password)
VALUES (1, true, 'Dimitrija Avramovic 4', false, '123asdq', 'Danica', 'Gazdic', '0691852001', 'test@email.com', '12345');
insert into passengers (id, active, address, blocked, profile_photo, name, last_name, phone_number, email, password)
VALUES (2, false, 'Maksima Gorkog 55', false, '123qwerty', 'Milos', 'Obradovic', '12341234', 'test2@email.com', '1234');

insert into drivers (id, active, address, blocked, email, last_name, name, password, phone_number, profile_photo)
VALUES (3, true, 'Adresa1', false, 'pp@gmail.com', 'Peric', 'Pera', '1234', '0641212', 'qwer');
insert into drivers (id, active, address, blocked, email, last_name, name, password, phone_number, profile_photo)
VALUES (6, false, 'Adresa2', false, 'pr@gmail.com', 'Radovanovic', 'Pera', '1234', '06412', 'qwer');
insert into drivers (id, active, address, blocked, email, last_name, name, password, phone_number, profile_photo)
VALUES (5, false, 'Adresa2', false, 'pa@gmail.com', 'Bro', 'Da', '1234', '062134412', 'qwer');

insert into working_hours (id, end_time, start_time, driver_id) VALUES (1, '2022-12-22T22:37:56.469083', '2022-12-22T17:37:56.469083', 3);

insert into locations (id, longitude, latitude, address) VALUES (1, 13.54, 11.11, 'Dimitrija Avramovica 3');
insert into locations (id, longitude, latitude, address) VALUES (2, 13.54, 11.11, 'NTP');

insert into passengers_favourite_locations (passenger_id, favourite_locations_id) VALUES (1, 1);

insert into documents (id, name, photo, driver_id) VALUES (1, 'Vozacka', 'qwer123', 3);

insert into vehicle_type (id, price, type) VALUES (1, 300, 1);

insert into vehicles (id, babies, pets, capacity, plates, vehicle_model, driver_id, vehicle_type_id) VALUES (1, true, true, 4, 'ns680hs', 'skoda fabia', 3, 1);

insert into rides (id, babies, end_time, panic, pets, ride_status, start_time, time_estimate, total_price, driver_id, vehicle_id)
VALUES (1, true, '2022-12-21T16:48:49.439927', false, false, 5, '2022-12-21T16:48:43.439927', 3, 350, 3, 1);
insert into rides (id, babies, end_time, panic, pets, ride_status, start_time, time_estimate, total_price, driver_id, vehicle_id)
VALUES (2, true, '2022-12-21T16:48:49.439927', true, false, 5, '2022-12-21T16:48:43.439927', 3, 350, 3, 1);
insert into rides (id, babies, end_time, panic, pets, ride_status, start_time, time_estimate, total_price, driver_id, vehicle_id)
VALUES (3, false, '2022-12-22T17:26:00.093044', false, false, 3, '2022-12-22T17:26:00.093044', 3, 0, 3, 1);

insert into ride_passengers (passenger_id, ride_id) VALUES (1, 1);
insert into ride_passengers (passenger_id, ride_id) VALUES (1, 2);
insert into ride_passengers (passenger_id, ride_id) VALUES (1, 3);

insert into messages (id, message, time_of_sending, message_type, receiver_id, ride_id, sender_id)
VALUES (1, 'Pozdrav svima', '2022-12-21T16:48:49.439927', 2, 3, 1, 1);

insert into panic (id, reason, time, ride_id, user_id) VALUES (1, 'Fatal crash', '2022-12-21T16:48:43.439927', 2, 1);

insert into rejections (id, reason, time, ride_id, user_id) VALUES (1, 'Putnik se nije pojavio', '2022-12-22T17:26:00.093044', 3, 1);

insert into reviews (id, comment, grade, driver_id, passenger_id, ride_id) VALUES (1, 'Svaka cst', 5, 3, 1, 1);

insert into routes (id, distance, departure, destination) VALUES (1, 4.2, 1, 2);
insert into rides_route (ride_id, route_id) VALUES (1, 1);


insert into user_activations (activation_id, creation_date_time, lifespan, user_id)
VALUES (1, '2022-12-22T17:37:56.469083', '2022-12-22T17:37:56.469083', 2);