// import connection
import db from "../config/database.js";

// insert Booking
export const insertBooking = (data,result) => {
    db.query("INSERT INTO booktable SET ?",data, (err,results)=> {
        if (err){
            console.log(err);
            result(err,null);
        }else{
            result(null,results[0]);
        }
    });
};