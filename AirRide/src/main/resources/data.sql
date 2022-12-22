insert into passengers (id, name, last_name, phone_number, email, password) VALUES (1,'Danica', 'Gazdic', '0691852001', 'test@email.com', '12345');
insert into passengers (id, name, last_name, phone_number, email, password) VALUES (2, 'Milos', 'Obradovic', '12341234', 'test2@email.com', '1234');

insert into locations (id, longitude, latitude, address) VALUES (1, 13.54, 11.11, 'Dimitrija Avramovica 3');

insert into passengers_favourite_locations (passenger_id, favourite_locations_id) VALUES (1, 1)