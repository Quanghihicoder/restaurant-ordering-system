import {
    getNewestId,
    insertBillStatus,
    getBillsByUser,
    getBillsByBill,
    getAll,
    updateStatus,
    updatePaid,
    cancelStatus
} from "../models/BillStatusModel.js";

// get newest Bill Status
export const showNewestStatusId=(req,res)=>{
    getNewestId((err,results)=> {
        if (err) {
            res.send(err);
        }else {
            res.json(results);
        }
    });
};

// create BillStatus
export const createBillStatus=(req,res)=>{
    const data = req.body;
    insertBillStatus(data,(err,results)=> {
        if (err) {
            res.send(err);
        }else {
            res.json(results);
        }
    });
};

// get Bills Status
export const getAllBillsByUser=(req,res)=>{
    getBillsByUser(req.params.id,(err,results)=> {
        if (err) {
            res.send(err);
        }else {
            res.json(results);
        }
    });
};

// get Bills Status
export const getAllBillsByBill=(req,res)=>{
    getBillsByBill(req.params.id,(err,results)=> {
        if (err) {
            res.send(err);
        }else {
            res.json(results);
        }
    });
};

// get Bills Status
export const getAllBills=(req,res)=>{
    getAll((err,results)=> {
        if (err) {
            res.send(err);
        }else {
            res.json(results);
        }
    });
};


// update Status
export const updateBillStatus=(req,res)=>{
    updateStatus(req.params.id,(err,results)=> {
        if (err) {
            res.send(err);
        }else {
            res.json(results);
        }
    });
};

// update Paid
export const updateBillPaid=(req,res)=>{
    updatePaid(req.params.id,(err,results)=> {
        if (err) {
            res.send(err);
        }else {
            res.json(results);
        }
    });
};

// cancel Status
export const cancelBillStatus=(req,res)=>{
    cancelStatus(req.params.id,(err,results)=> {
        if (err) {
            res.send(err);
        }else {
            res.json(results);
        }
    });
};