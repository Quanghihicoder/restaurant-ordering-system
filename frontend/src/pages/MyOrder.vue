<template>
    <div class="my-order-container" :class="filterBills.length > 0 ? '' : 'fit-screen'">
        <div v-if="filterBills.length > 0" class="my-order-cards">
            <div v-for="b in filterBills.slice().reverse()" class="card" :key="b.bill_id">
                <div class="card-head d-flex flex-wrap flex-sm-nowrap justify-content-between">
                    <div>
                        <span>Order No - </span>
                        <span>{{ b.bill_id }}</span>
                    </div>
                    <button @click="sendBillId(b.bill_id)">show order details</button>
                </div>

                <div class="d-flex flex-wrap flex-sm-nowrap justify-content-between card-summary">
                    <div class="w-100 text-center py-1 px-2"><span>Paid:</span>{{ " " + b.bill_paid }}
                    </div>
                    <div class="w-100 text-center py-1 px-2"><span>Status:</span>{{ " " + avaiableStatus[b.bill_status]
                    }}
                    </div>
                    <div class="w-100 text-center py-1 px-2"><span>When:</span> {{ b.bill_when }}</div>
                </div>
                <div class="d-flex flex-wrap flex-sm-nowrap justify-content-between card-summary">

                    <div class="w-100 text-center py-1 px-2"><span>Total:</span> ${{ b.bill_total }}</div>
                    <div class="w-100 text-center py-1 px-2"><span>Address:</span>{{ " " + b.bill_address }}
                    </div>
                    <div class="w-100 text-center py-1 px-2"><span>Phone:</span>{{ " " + b.bill_phone }}
                    </div>
                </div>

                <div class="card-body">
                    <div class="steps d-flex flex-wrap flex-sm-nowrap justify-content-between">
                        <div class="step" :class="b.bill_status >= 1 ? 'completed' : ''">
                            <div class="step-icon-wrap">
                                <div class="step-icon"><i class="fa-solid fa-utensils"></i></div>
                            </div>
                            <h4 class="step-title">Confirmed</h4>
                        </div>
                        <div class="step" :class="b.bill_status >= 2 ? 'completed' : ''">
                            <div class="step-icon-wrap">
                                <div class="step-icon"><i class="fa-solid fa-fire-burner"></i></div>
                            </div>
                            <h4 class="step-title">Preparing</h4>
                        </div>
                        <div class="step" :class="b.bill_status >= 3 ? 'completed' : ''">
                            <div class="step-icon-wrap">
                                <div class="step-icon"><i class="fa-solid fa-list-check"></i></div>
                            </div>
                            <h4 class="step-title">Checking</h4>
                        </div>
                        <div class="step" :class="b.bill_status >= 4 ? 'completed' : ''">
                            <div class="step-icon-wrap">
                                <div class="step-icon"><i class="fa-solid fa-route"></i></div>
                            </div>
                            <h4 class="step-title">Delivering</h4>
                        </div>
                        <div class="step" :class="b.bill_status >= 5 ? 'completed' : ''">
                            <div class="step-icon-wrap">
                                <div class="step-icon"><i class="fa-solid fa-house"></i></div>
                            </div>
                            <h4 class="step-title">Delivered</h4>
                        </div>
                    </div>
                </div>
            </div>
        </div>


        <div v-else class="box-content row no-food">
            <div class="content">
                <h2 style="color: #057835fa;">You do not have any orders yet</h2>
            </div>
            <div>
                <img src="../assets/images/no-orders.png" alt="" />
            </div>
            <router-link class="btn" to="/menu">Order now!</router-link>
        </div>

        <OrderDetails v-if="showOrderDetails" :bill="sendId">
            <button class="btn" @click="closeView">X</button>
        </OrderDetails>
    </div>

</template>


<script>
import OrderDetails from "@/components/OrderDetails.vue";
import axios from "axios";
import { mapState } from "vuex";
export default {
    name: 'MyOrder',

    data() {
        return {
            avaiableStatus: ["cancel", "confirmed", "preparing", "checking", "delivering", "delivered"],
            allBills: [],

            showOrderDetails: false,
            sendId: null,

            interval: "",
        }
    },

    created() {
        this.getAllBills();
    },

    mounted: function () {
        this.autoUpdate();
    },

    beforeUnmount() {
        clearInterval(this.interval)
    },

    computed: {
        ...mapState(["allFoods", "user"]),

        filterBills: function () {
            return this.allBills.filter((b) => b.bill_status < 6 && b.bill_status > 0);
        },
    },

    methods: {
        async getAllBills() {
            if (this.user) {
                this.allBills = (await axios.get('/billstatus/user/' + this.user.user_id)).data;
            }
        },

        sendBillId: function (id) {
            this.sendId = id
            this.showOrderDetails = !this.showOrderDetails;
        },

        closeView: function () {
            this.showOrderDetails = !this.showOrderDetails;
        },

        autoUpdate: function () {
            this.interval = setInterval(function () {
                this.getAllBills();
            }.bind(this), 1000);
        }
    },
    components: { OrderDetails }
}
</script>

<style scoped>
.my-order-container {
    padding: 2rem 9%;
    background: #fff;
    height: 100%;
}

.my-order-container.fit-screen {
    height: 90vh;
}

.my-order-cards {
    margin-bottom: 2rem;
}

.card {
    margin-bottom: 3px;
}

.card-head {
    padding: 12px 0px;
    color: white;
    font-size: 16px;
    background: #27ae60;

}

.card-head span:first-of-type {
    margin-left: 20px;
}

.card-head button {
    background-color: inherit;
    color: white;
    margin-right: 20px;
    font-weight: 500;
}

.card-head button:hover {
    color: #f38609;
}

.card-summary {
    padding: 12px 10px;
    background: #eee;
    font-size: 14px;
}

.steps .step {
    display: block;
    width: 100%;
    margin-bottom: 35px;
    text-align: center
}

.steps .step .step-icon-wrap {
    display: block;
    position: relative;
    width: 100%;
    height: 80px;
    text-align: center
}

.steps .step .step-icon-wrap::before,
.steps .step .step-icon-wrap::after {
    display: block;
    position: absolute;
    top: 50%;
    width: 50%;
    height: 3px;
    margin-top: -1px;
    background-color: #e1e7ec;
    content: '';
    z-index: 1
}

.steps .step .step-icon-wrap::before {
    left: 0
}

.steps .step .step-icon-wrap::after {
    right: 0
}

.steps .step .step-icon {
    display: inline-block;
    position: relative;
    width: 80px;
    height: 80px;
    border: 1px solid #e1e7ec;
    border-radius: 50%;
    background-color: #f5f5f5;
    color: #374250;
    font-size: 38px;
    line-height: 81px;
    z-index: 5
}

.steps .step .step-title {
    margin-top: 16px;
    margin-bottom: 0;
    color: #606975;
    font-size: 14px;
    font-weight: 500
}

.steps .step:first-child .step-icon-wrap::before {
    display: none
}

.steps .step:last-child .step-icon-wrap::after {
    display: none
}

.steps .step.completed .step-icon-wrap::before,
.steps .step.completed .step-icon-wrap::after {
    background-color: #0da9ef
}

.steps .step.completed .step-icon {
    border-color: #0da9ef;
    background-color: #0da9ef;
    color: #fff
}

.no-food {
    text-align: center;
    justify-content: center;
    display: block;
    width: 100%;
    height: 100%;
    margin: auto;
}

.no-food a {
    margin-top: 20px;
    margin-left: -10px;
}

@media (max-width: 320px) {
    .my-order-container {
        padding: 0px;
    }

    .card-head {
        font-size: 14px;
    }

    .no-food .content h2 {
        font-size: 14px;
    }


}

@media (max-width: 576px) {
    .my-order-container {
        padding: 1rem 4.5%;
    }

    .flex-sm-nowrap .step .step-icon-wrap::before,
    .flex-sm-nowrap .step .step-icon-wrap::after {
        display: none
    }

    .card {
        margin-top: 20px;
    }

    .no-food div img {
        width: 85vw;
    }
}

@media (max-width: 768px) {

    .flex-md-nowrap .step .step-icon-wrap::before,
    .flex-md-nowrap .step .step-icon-wrap::after {
        display: none
    }
}

@media (max-width: 991px) {

    .flex-lg-nowrap .step .step-icon-wrap::before,
    .flex-lg-nowrap .step .step-icon-wrap::after {
        display: none
    }
}

@media (max-width: 1200px) {

    .flex-xl-nowrap .step .step-icon-wrap::before,
    .flex-xl-nowrap .step .step-icon-wrap::after {
        display: none
    }
}

.bg-faded,
.bg-secondary {
    background-color: #f5f5f5 !important;
}
</style>