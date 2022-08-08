// import connection
import db from "../config/database.js";

// get all Foods
export const getFoods = (result) => {
    db.query("SELECT * FROM food", (err,results)=> {
        if (err){
            console.log(err);
            result(err,null);
        }else{
            result(null,results);
        }
    });
};

// get single Foods
export const getFoodById = (id,result) => {
    db.query("SELECT * FROM food WHERE food_id = ?",[id], (err,results)=> {
        if (err){
            console.log(err);
            result(err,null);
        }else{
            result(null,results[0]);
        }
    });
};

// insert Food
export const insertFood = (data,result) => {
    db.query("INSERT INTO food SET ?",data, (err,results)=> {
        if (err){
            console.log(err);
            result(err,null);
        }else{
            result(null,results[0]);
        }
    });
};

// update Food
export const updateFoodById = (data,id,result) => {
    db.query("UPDATE food SET food_name = ?, food_price = ? WHERE food_id = ?",[data.food_name, data.food_price, id], (err,results)=> {
        if (err){
            console.log(err);
            result(err,null);
        }else{
            result(null,results);
        }
    });
};


// delete Food
export const deleteFoodById = (id,result) => {
    db.query("DELETE FROM food WHERE food_id = ?",[id], (err,results)=> {
        if (err){
            console.log(err);
            result(err,null);
        }else{
            result(null,results);
        }
    });
};