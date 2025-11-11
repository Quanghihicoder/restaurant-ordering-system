// import express
import express from "express";
// import functions from controller
import {
  showFoods,
  showFoodById,
  createFood,
  updateFood,
  deleteFood,
} from "../controllers/food.js";

import { showAUser, createAccount } from "../controllers/user.js";

import {
  addItems,
  getItem,
  updateItem,
  allItems,
  deleteItem,
  deleteItems,
} from "../controllers/cart.js";

import { createBooking } from "../controllers/booktable.js";

import {
  createBillDetails,
  getBillDetailsById,
} from "../controllers/billdetails.js";

import {
  showNewestStatusId,
  createBillStatus,
  getAllBillsByUser,
  getAllBillsByBill,
  getAllBills,
  updateBillStatus,
  updateBillPaid,
  cancelBillStatus,
} from "../controllers/billstatus.js";

// init express router
const router = express.Router();

////////////////////////// FOOD ////////////////////////////////
// get all Food
router.get("/foods", showFoods);

// get single Food
router.get("/foods/:id", showFoodById);

// create Food
router.post("/foods", createFood);

// update Food
router.put("/foods/:id", updateFood);

// delete Food
router.delete("/foods/:id", deleteFood);

////////////////////////// USER ////////////////////////////////
// get all user
router.get("/users/:email", showAUser);

// create account
router.post("/users/", createAccount);

////////////////////////// CART ////////////////////////////////
// add to cart
router.post("/cartItem", addItems);

// get a item in cart
router.get("/cartItem/:user_id/:food_id", getItem);

// get all items by user id
router.get("/cartItem/:id", allItems);

// update item qty
router.put("/cartItem/", updateItem);

// delete a item in cart
router.delete("/cartItem/:user_id/:food_id", deleteItem);

// delete all items in cart
router.delete("/cartItem/:id", deleteItems);

////////////////////////// Booking ////////////////////////////////
router.post("/booking", createBooking);

////////////////////////// Bill Details ////////////////////////////////
router.post("/billdetails", createBillDetails);
router.get("/billdetails/:id", getBillDetailsById);

////////////////////////// Bill Status ////////////////////////////////
router.get("/billstatus/new", showNewestStatusId);
router.post("/billstatus", createBillStatus);
router.get("/billstatus/user/:id", getAllBillsByUser);
router.get("/billstatus/bill/:id", getAllBillsByBill);
router.get("/billstatus", getAllBills);
router.put("/billstatus/:id", updateBillStatus);
router.put("/billstatus/paid/:id", updateBillPaid);
router.put("/billstatus/cancel/:id", cancelBillStatus);

// export default router
export default router;
