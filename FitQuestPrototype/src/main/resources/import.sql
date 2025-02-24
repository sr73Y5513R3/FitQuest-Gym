INSERT INTO NIVEL (ID, NOMBRE) VALUES (NEXTVAL('nivel_seq'), 'Principiante');
INSERT INTO NIVEL (ID, NOMBRE) VALUES (NEXTVAL('nivel_seq'), 'Intermedio');

INSERT INTO EJERCICIO (ID, NOMBRE, DESCRIPCION, SERIES, REPETICIONES, DURACION, URL_IMAGENES) VALUES (NEXTVAL('ejercicio_seq'), 'Ejercicio de pecho', 'Consiste en coger una barra y levantarla por encima de nuestro pecho', 4, 10, 30, 'No hay');
INSERT INTO EJERCICIO (ID, NOMBRE, DESCRIPCION, SERIES, REPETICIONES, DURACION, URL_IMAGENES) VALUES (NEXTVAL('ejercicio_seq'), 'Ejercicio de tricep', 'Consiste en levantar una pesa por encima de nuestra cabeza', 4, 10, 20, 'No hay');

INSERT INTO MATERIAL (ID, NOMBRE, DESCRIPCION, TIPO) VALUES (NEXTVAL('material_seq'),'Pesa de 50', 'Mancuerna que utiliza Rafa para hacer press de banca en uno de sus brazos', 'PESA' );
INSERT INTO MATERIAL (ID, NOMBRE, DESCRIPCION, TIPO) VALUES (NEXTVAL('material_seq'), 'Cinta de correr', 'Máquina utilizada para correr y mejorar resistencia cardiovascular', 'MAQUINA');
INSERT INTO MATERIAL (ID, NOMBRE, DESCRIPCION, TIPO) VALUES (NEXTVAL('material_seq'), 'Pesa rusa 20kg', 'Pesa utilizada para ejercicios de fuerza y estabilidad', 'PESA');

