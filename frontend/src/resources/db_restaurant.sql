-- database name: "db_restaurant"

CREATE TABLE food( 
    food_id INT(11) PRIMARY KEY AUTO_INCREMENT, 
    food_name VARCHAR(255), 
    food_star VARCHAR(255),
    food_vote VARCHAR(255),
    food_price VARCHAR(255),
    food_discount VARCHAR(255),
    food_desc VARCHAR(255),
    food_status VARCHAR(255),
    food_type VARCHAR(255),
    food_category VARCHAR(255),
    food_src VARCHAR(255)
) ENGINE=INNODB;

INSERT INTO food (food_name, food_star, food_vote, food_price, food_discount, food_desc, food_status, food_type, food_category, food_src)
VALUES("carne asada tacos","4.5", "999", "12.00", "0.00", "03 pieces per serving", "best seller", "meat", "taco", "taco/taco-1.png"),
("shrimp tacos","4.5", "999", "15.00", "3.00", "03 pieces per serving", "best seller", "meat", "taco", "taco/taco-2.png"),
("barbacoa tacos","4.5","500","12.00","0.00","03 pieces per serving","best seller","meat","taco","taco/taco-3.png"),
("tacos al pastor","4.5","999","13.00","2.00","03 pieces per serving","best seller","meat","taco","taco/taco-4.png"),
("tinga tacos","4","500","11.00","0.00","03 pieces per serving","normal","meat","taco","taco/taco-5.png"),
("campechanos tacos","4","500","11.00","1.00","03 pieces per serving","new dishes","meat","taco","taco/taco-6.png"),
("carnitas tacos","4.5","500","14.00","2.00","03 pieces per serving","seasonal dishes online only","meat","taco","taco/taco-7.png"),
("vegan tacos","4.5","100","9.00","2.00","03 pieces per serving","new dishes","vegan","taco","taco/taco-8.png"),
("wet burrito","4.5","600","14.00","0.00","01 roll per serving","new dishes","meat","burrito","burrito/burrito-1.png"),
("poncho burrito","4.5","999","15.00","3.00","01 roll per serving","best seller","meat","burrito","burrito/burrito-2.png"),
("bean & cheese burrito","4.5","999","14.00","0.00","01 roll per serving","best seller","vegan","burrito","burrito/burrito-3.png"),
("breakfast burrito","4.5","999","12.00","2.00","01 roll per serving","new dishes","meat","burrito","burrito/burrito-4.png"),
("california burrito","4.5","999","14.00","0.00","01 roll per serving","best seller","meat","burrito","burrito/burrito-5.png"),
("chimichanga","4","400","12.00","2.00","01 roll per serving","seasonal dishes","meat","burrito","burrito/burrito-6.png"),
("nacho tots","4","699","12.00","2.00","01 tray per serving","best seller","meat","nachos","nachos/nachos-1.png"),
("root beer pork nachos","4.5","999","12.00","0.00","01 tray per serving","best seller","meat","nachos","nachos/nachos-2.png"),
("shrimp nachos","4.5","999","17.00","2.00","01 tray per serving","best seller","meat","nachos","nachos/nachos-3.png"),
("chicken nachos","4.5","999","11.00","0.00","01 tray per serving","best seller","meat","nachos","nachos/nachos-4.png"),
("only nachos","4","999","7.00","2.00","01 tray per serving","normal","vegan","nachos","nachos/nachos-5.png"),
("pico de gallo","4.5","999","5.00","2.00","01 bowl per serving","best seller","vegan","nachos","nachos/salsa-1.png"),
("salsa guille","4","699","5.00","2.00","01 bowl per serving","best seller","vegan","nachos","nachos/salsa-2.png"),
("tomatillo salsa","4.5","499","5.00","2.00","01 bowl per serving","seasonal dishes","vegan","nachos","nachos/salsa-3.png"),
("roasted tomato salsa","4.5","999","5.00","2.00","01 bowl per serving","best seller","vegan","nachos","nachos/salsa-4.png"),
("guacamole","4.5","699","5.00","2.00","01 bowl per serving","best seller","vegan","nachos","nachos/salsa-5.png"),
("corn salad","3.5","699","5.00","1.00","01 bowl per serving","new dishes seasonal dishes","vegan","sides","side/side-1.png"),
("keto taquitos","4.5","999","9.00","0.00","05 pieces per serving","best seller","meat","sides","side/side-2.png"),
("mexican rice","4","200","5.00","0.00","01 bowl per serving","normal","vegan","sides","side/side-3.png"),
("cilantro lime rice","4","100","5.00","0.00","01 bowl per serving","new dishes","vegan","sides","side/side-4.png"),
("chicken tortilla soup","3.5","299","10.00","2.00","01 bowl per serving","new dishes","meat","sides","side/side-5.png"),
("Churros","4.5","999","7.00","0.00","05 pieces per serving","best seller","vegan","dessert","dessert/dessert-1.png"),
("Fried Ice Cream","4.5","999","5.00","1.00","01 piece per serving","best seller","vegan","dessert","dessert/dessert-2.png"),
("Dulce de Leche","4.5","50","4.00","0.00","01 bowl per serving","new dishes","vegan","dessert","dessert/dessert-3.png"),
("Sweet Corn Cake","3","599","4.00","1.00","02 pieces per serving","seasonal dishes online only","vegan","dessert","dessert/dessert-4.png"),
("Sopapillas","4","199","4.00","0.00","10 pieces per serving","normal","vegan","dessert","dessert/dessert-5.png"),
("Conchas","4","299","5.00","0.00","10 pieces per serving","normal","vegan","dessert","dessert/dessert-6.png"),
("Horchata","4.5","999","4.00","0.00","01 glass per serving","normal","vegan","dessert","dessert/dessert-7.png"),
("Margarita","4.5","999","5.00","0.00","01 glass per serving","best seller","vegan","drink","drink/drink-1.png"),
("Michelada","4.5","999","5.00","0.00","01 glass per serving","best seller","vegan","drink","drink/drink-2.png"),
("paloma","4.5","599","5.00","0.00","01 glass per serving","new dishes seasonal dishes","vegan","drink","drink/drink-3.png"),
("Atole","4","999","5.00","1.00","01 glass per serving","best seller","vegan","drink","drink/drink-4.png"),
("fruit detox","3.5","999","3.00","0.00","01 glass per serving","seasonal dishes best seller","vegan","drink","drink/drink-5.png"),
("Coca cola","4.5","9999","3.00","0.00","01 glass per serving","best seller","vegan","drink","drink/drink-6.png"); 


CREATE TABLE user( 
    user_id INT(11) PRIMARY KEY AUTO_INCREMENT, 
    user_name VARCHAR(255), 
    user_email VARCHAR(255),
    user_phone VARCHAR(255),
    user_password VARCHAR(255),
    user_birth VARCHAR(255),
    user_gender VARCHAR(255)
) ENGINE=INNODB;


CREATE TABLE cart (
  user_id INT,
  food_id INT,
  item_qty INT,
  primary key (user_id, food_id)
);


CREATE TABLE booktable( 
    book_id INT(11) PRIMARY KEY AUTO_INCREMENT, 
    book_name VARCHAR(255), 
    book_phone VARCHAR(255),
    book_people INT,
    book_tables INT,
    user_id INT,
    book_when VARCHAR(255),
    book_note TEXT
) ENGINE=INNODB;


CREATE TABLE billdetails (
  bill_id INT,
  food_id INT,
  item_qty INT,
  primary key (bill_id, food_id)
);

CREATE TABLE billstatus (
  bill_id INT,
  user_id INT,
  bill_phone VARCHAR(255),
  bill_address TEXT,
  bill_when VARCHAR(255),
  bill_method VARCHAR(255),
  bill_discount INT,
  bill_delivery INT,
  bill_total INT,
  bill_paid VARCHAR(255),
  bill_status INT,
  primary key (bill_id)
);