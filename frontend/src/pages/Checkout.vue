<template>
    <div class="checkout-container">
        <div class="checkout-form-container">
            <form id="checkoutForm" @submit="handleSubmit" novalidate autocomplete="off">
                <div class="checkout-heading">
                    <h3>Few more step to place your order<span>Total</span></h3>
                    <h3 v-if="user">{{ user.user_name }}'s Order<span>${{ calculateSummaryPrice()[3] }}</span></h3>
                </div>

                <div class="form-group details-group">
                    <h4>Shipping Details</h4>
                    <div class="form-group">
                        <input type="text" name="coPhone" id="coPhone" placeholder="Phone number" class="form-control"
                            v-model="checkoutObj.phone" />
                        <p class="error-mess" v-if="errorObj.phoneErr.length > 0">{{ errorObj.phoneErr[0] }}</p>
                    </div>

                    <div class="form-group">
                        <input type="text" name="coAddress" id="coAddress" placeholder="Address in Hanoi, Vietnam"
                            class="form-control" v-model="checkoutObj.address" />
                        <p class="error-mess" v-if="errorObj.addressErr.length > 0">{{ errorObj.addressErr[0] }}</p>
                    </div>
                </div>

                <div class="form-group details-group">
                    <h4>Payment Method</h4>
                    <div class="form-group">
                        <div class="form-group">
                            <input type="radio" name="payment" value="cash" id="paymentCash"
                                v-model="checkoutObj.paymentMethod" /><span>Cash</span>
                            <input type="radio" name="payment" value="card" id="paymentCard"
                                v-model="checkoutObj.paymentMethod" /><span>Card (Visa)</span>
                        </div>
                        <p class="error-mess" v-if="errorObj.payErr.length > 0">{{ errorObj.payErr[0] }}</p>
                    </div>


                    <div v-if="checkoutObj.paymentMethod == 'card'">
                        <div class="form-group">
                            <input type="text" name="coCardNum" placeholder="Enter your card number" id="coCardNum"
                                class="form-control" v-model="cardObj.number" size="16" maxlength="16" />
                            <p class="error-mess" v-if="errorObj.numErr.length > 0">{{ errorObj.numErr[0] }}</p>
                        </div>

                        <div class="form-group">
                            <input v-upcase type="text" name="coCardName" placeholder="Enter the name in your card "
                                id="coCardName" class="form-control" v-model="cardObj.name" />
                            <p class="error-mess" v-if="errorObj.nameErr.length > 0">{{ errorObj.nameErr[0] }}</p>
                        </div>

                        <div class="form-group">
                            <div class="form-control">
                                <span
                                    style="font-size: 1.6rem; position: absolute; margin-left: -5px; margin-top: -11px;">Expiry
                                    Date:
                                </span>
                                <input
                                    style="position: absolute; margin-left: 100px; margin-top: -12px; background: inherit;"
                                    type="month" name="coCardEx" id="coCardEx" v-model="cardObj.expiryDate"
                                    @click="availableTime()" />
                            </div>
                            <p class="error-mess" v-if="errorObj.exDateErr.length > 0">{{ errorObj.exDateErr[0] }}</p>
                        </div>

                        <div class="form-group">
                            <input type="text" name="coCardCvv" placeholder="CVV" id="coCardCvv" class="form-control"
                                v-model="cardObj.cvv" />
                            <p class="error-mess" v-if="errorObj.cvvErr.length > 0">{{ errorObj.cvvErr[0] }}</p>
                        </div>
                    </div>
                </div>

                <div v-if="user" class="form-group">
                    <input type="submit" value="CONFIRM & PAY" class="btn"
                        :disabled="filterFoods.length ? false : true" />
                </div>
            </form>
        </div>
    </div>
</template>

<script>
import axios from "axios";
import { mapState } from "vuex";
export default {
    name: "Checkout",

    data() {
        return {
            checkoutObj: { phone: "", address: "", paymentMethod: "" },
            cardObj: { number: "", name: "", expiryDate: "", cvv: "" },
            errorObj: { phoneErr: [], addressErr: [], payErr: [], numErr: [], nameErr: [], exDateErr: [], cvvErr: [] },
            cartItem: [],
            itemQuantity: [],
        }
    },

    created() {
        this.getAllCartItem();
    },

    computed: {
        ...mapState(["allFoods", "user"]),

        filterFoods: function () {
            return this.allFoods.filter(
                (f) => this.matchID(f, this.cartItem)
            );
        },
    },

    methods: {
        availableTime: function () {
            var now = new Date();
            var currentMonth = ("0" + (now.getMonth() + 1)).slice(-2);

            var minRange = now.getFullYear() + "-" + currentMonth;
            var maxRange = (now.getFullYear() + 10) + "-" + currentMonth;

            document.getElementById("coCardEx").setAttribute("min", minRange);
            document.getElementById("coCardEx").setAttribute("max", maxRange);
        },

        matchID: function (food, cartArray) {
            let temp = "";
            cartArray.forEach(element => {
                if (parseInt(food.food_id) == element) {
                    temp = food
                }
            });
            return temp
        },

        calculateSummaryPrice: function () {
            let subtotal = 0;
            let discount = 0;
            let delivery = 15;
            let i = 0;
            while (i < this.itemQuantity.length) {
                subtotal = subtotal + parseInt(this.filterFoods[i].food_price) * this.itemQuantity[i]
                discount = discount + parseInt(this.filterFoods[i].food_discount) * this.itemQuantity[i]
                i = i + 1
            }
            if (!this.filterFoods.length) {
                delivery = 0
            }
            let total = subtotal - discount + delivery;
            return [subtotal, discount, delivery, total];
        },

        async getAllCartItem() {
            if (this.user) {
                let existItem = await axios.get('/cartItem/' + this.user.user_id);
                existItem.data.forEach(element => {
                    this.cartItem.push(element.food_id);
                    this.itemQuantity.push(element.item_qty);
                });
            }
        },

        resetCheckErr: function () {
            this.errorObj.phoneErr = [];
            this.errorObj.addressErr = [];
            this.errorObj.payErr = [];
            this.errorObj.numErr = [];
            this.errorObj.nameErr = [];
            this.errorObj.exDateErr = [];
            this.errorObj.cvvErr = [];
        },

        checkEmptyErr: function () {
            for (var typeErr in this.errorObj) {
                if (this.errorObj[typeErr].length != 0) {
                    return false;
                }
            }
            return true;
        },

        inputUpcase: function (e) {
            e.target.value = e.target.value.toUpperCase()
        },

        checkForm: function () {
            this.resetCheckErr();

            // Phone validate
            if (!this.checkoutObj.phone) {
                this.errorObj.phoneErr.push('Entering phone number is required');
            }
            else {
                if (!this.checkoutObj.phone.startsWith('84')) {
                    this.errorObj.phoneErr.push('Phone numbers must start with 84');
                }

                if (this.checkoutObj.phone.length != 11) {
                    this.errorObj.phoneErr.push('Phone numbers must have exactly 11 digits');
                }

                if (!/[0-9]{11}/.test(this.checkoutObj.phone)) {
                    this.errorObj.phoneErr.push('Phone numbers can only contain numbers');
                }
            }

            // Address validate
            if (!this.checkoutObj.address) {
                this.errorObj.addressErr.push('Entering address is required');
            }

            // Card validate
            if (!this.checkoutObj.paymentMethod) {
                this.errorObj.payErr.push('Selecting payment method is required');
            }
            else if (this.checkoutObj.paymentMethod == "card") {
                if (!this.cardObj.number) {
                    this.errorObj.numErr.push('Entering card number is required');
                }
                else {
                    if (!this.cardObj.number.startsWith('4')) {
                        this.errorObj.numErr.push('Visa card numbers must start with 4');
                    }

                    if (this.cardObj.number.length != 16) {
                        this.errorObj.numErr.push('Visa card numbers must have exactly 16 digits');
                    }

                    if (!/[0-9]{16}/.test(this.cardObj.number)) {
                        this.errorObj.numErr.push('Visa card numbers can only contain numbers');
                    }
                }

                if (!this.cardObj.name) {
                    this.errorObj.nameErr.push('Entering name is required');
                }
                else {
                    if (!/^[A-Za-z]+$/.test(this.cardObj.name.replace(/\s/g, ""))) {
                        this.errorObj.nameErr.push('A name can only contain letters');
                    }
                }

                if (!this.cardObj.expiryDate) {
                    this.errorObj.exDateErr.push('Entering expiry date is required');
                }


                if (!this.cardObj.cvv) {
                    this.errorObj.cvvErr.push('Entering cvv code is required');
                }
                else {
                    if (this.cardObj.cvv.length != 3) {
                        this.errorObj.cvvErr.push('Cvv code must have exactly 3 digits');
                    }

                    if (!/[0-9]{3}/.test(this.cardObj.cvv)) {
                        this.errorObj.cvvErr.push('Cvv code can only contain numbers');
                    }
                }
            } else if (this.checkoutObj.paymentMethod == "cash") {
                this.cardObj.number = "";
                this.cardObj.name = "";
                this.cardObj.expiryDate = "";
                this.cardObj.cvv = "";

                this.errorObj.numErr = [];
                this.errorObj.nameErr = [];
                this.errorObj.exDateErr = [];
                this.errorObj.cvvErr = [];
            }
        },

        isPaid: function () {
            if (this.checkoutObj.paymentMethod == "cash") {
                return "false"
            }
            else if (this.checkoutObj.paymentMethod == "card") {
                return "true"
            }
        },

        async sendBillDetails(billId, foodId, qty) {
            let billDetails = {
                bill_id: parseInt(billId),
                food_id: parseInt(foodId),
                item_qty: parseInt(qty)
            }

            await axios.post("/billdetails", billDetails);
        },

        async handleSubmit(e) {
            this.checkForm();

            if (!this.checkEmptyErr()) {
                e.preventDefault();
            } else {
                e.preventDefault();
                let billId = (await axios.get("/billstatus/new")).data;

                if (billId == "") {
                    billId = 1
                } else {
                    billId = parseInt(billId.bill_id) + 1
                }

                this.cartItem.forEach((foodId, index) => {
                    this.sendBillDetails(billId, foodId, this.itemQuantity[index])
                });

                var now = new Date();
                var day = ("0" + now.getDate()).slice(-2);
                var month = ("0" + (now.getMonth() + 1)).slice(-2);
                var hour = ("0" + (now.getHours())).slice(-2);
                var min = ("0" + (now.getMinutes())).slice(-2);
                var currentTime = now.getFullYear() + "-" + month + "-" + day + "T" + hour + ":" + min;

                let billStatus = {
                    bill_id: parseInt(billId),
                    user_id: parseInt(this.user.user_id),
                    bill_phone: this.checkoutObj.phone,
                    bill_address: this.checkoutObj.address,
                    bill_when: currentTime,
                    bill_method: this.checkoutObj.paymentMethod,
                    bill_discount: parseInt(this.calculateSummaryPrice()[1]),
                    bill_delivery: parseInt(this.calculateSummaryPrice()[2]),
                    bill_total: parseInt(this.calculateSummaryPrice()[3]),
                    bill_paid: this.isPaid(),
                    bill_status: 1
                }

                axios.post("/billstatus", billStatus);
                axios.delete("/cartItem/" + this.user.user_id);

                this.cartItem = [];
                this.itemQuantity = [];

                this.$router.push("/thank");

            }
        }
    }
};
</script>

<script setup>
// enables v-focus in templates
const vUpcase = {
    mounted(el) {
        el.style.textTransform = "uppercase";
    }
}
</script>

<style scoped>
.checkout-container {
    padding: 2rem 9%;
}

.checkout-container .checkout-form-container {
    background: #fff;
    height: 90vh;
}

.checkout-container .checkout-form-container form {
    position: absolute;
    top: 50%;
    left: 50%;
    transform: translate(-50%, -42%);
    max-width: 70rem;
    width: 100%;
    box-shadow: 0 1rem 1rem rgba(0, 0, 0, 0.05);
    border: 0.1rem solid rgba(0, 0, 0, 0.2);
    padding: 2rem;
    border-radius: 0.5rem;
    animation: fadeUp 0.4s linear;
}

.checkout-container .checkout-form-container form h3 {
    padding-bottom: 1rem;
    font-size: 2rem;
    text-transform: uppercase;
    color: #130f40;
    margin: 0;
}

.checkout-container .checkout-form-container form .form-control {
    margin: 0.7rem 0;
    border-radius: 0.5rem;
    background: #f7f7f7;
    padding: 2rem 1.2rem;
    font-size: 1.6rem;
    color: #130f40;
    text-transform: none;
    width: 100%;
    border: none;
}

.checkout-container .checkout-form-container form label {
    font-size: 2rem;
    margin: 0;
    padding: 0;
}

.checkout-container .checkout-form-container form span {
    font-size: 18px;
    padding-left: 5px;
    padding-right: 40px;
}

.checkout-container .checkout-form-container form .btn {
    margin: 1rem 0;
    width: 100%;
    text-align: center;
}

.checkout-container .checkout-form-container form p {
    padding-top: 1rem;
    font-size: 1.5rem;
    color: #666;
    margin: 0;
}

.checkout-container .checkout-form-container form p a {
    color: #27ae60;
}

.checkout-container .checkout-form-container form p a:hover {
    color: #130f40;
    text-decoration: underline;
}

.checkout-container .checkout-form-container form .form-group {
    margin: 0;
}

.checkout-container .checkout-form-container form .form-group.details-group {
    margin-top: 40px;
}

.checkout-container .checkout-form-container form .form-group .error-mess {
    font-size: 1.5rem;
    position: relative;
    color: rgb(243, 47, 47);
    margin: 0;
    padding: 0;
}

.checkout-container .checkout-form-container form .checkout-heading h3 {
    display: flex;
    justify-content: space-between;
}

.checkout-container .checkout-form-container form .checkout-heading h3 span {
    padding-right: 0px;
    color: #f38609;
}

.checkout-container .checkout-form-container form .checkout-heading h3:first-of-type span {
    padding-right: 0px;
    color: #130f40;
}
</style>
