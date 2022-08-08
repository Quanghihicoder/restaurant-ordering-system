<template>
    <div class="promotions">
        <div class="heading">
            <span>promotions</span>
            <h3>Best quality with reasonable price</h3>
        </div>

        <div class="promotions-item">
            <div class="table-responsive">
                <table class="table table-bordered text-center">
                    <thead>
                        <tr class="bg-light-gray">
                            <th class="text-uppercase">Time</th>
                            <th class="text-uppercase">Monday</th>
                            <th class="text-uppercase">Tuesday</th>
                            <th class="text-uppercase">Wednesday</th>
                            <th class="text-uppercase">Thursday</th>
                            <th class="text-uppercase">Friday</th>
                            <th class="text-uppercase">Saturday</th>
                            <th class="text-uppercase">Sunday</th>
                        </tr>
                    </thead>
                    <tbody>
                        <tr>
                            <td class="align-middle">07:00 - 09:00</td>
                            <td>
                                <span class="bg-brown activity-name">Breakfast time</span>
                                <div class="activity-time">Discount 10%</div>
                            </td>
                            <td>
                                <span class="bg-brown activity-name">Breakfast time</span>
                                <div class="activity-time">Discount 10%</div>
                            </td>

                            <td>
                                <span class="bg-brown activity-name">Breakfast time</span>
                                <div class="activity-time">Discount 10%</div>
                            </td>
                            <td>
                                <span class="bg-brown activity-name">Breakfast time</span>
                                <div class="activity-time">Discount 10%</div>
                            </td>
                            <td>
                                <span class="bg-brown activity-name">Breakfast time</span>
                                <div class="activity-time">Discount 10%</div>
                            </td>
                            <td class="bg-light-gray">

                            </td>
                            <td class="bg-light-gray">

                            </td>
                        </tr>

                        <tr>
                            <td class="align-middle">10:00 - 14:00</td>
                            <td>
                                <span class="bg-beige activity-name">Happy Lunch</span>
                                <div class="activity-time">Free Drink</div>
                            </td>
                            <td class="bg-light-gray">

                            </td>
                            <td>
                                <span class="bg-beige activity-name">Happy Lunch</span>
                                <div class="activity-time">Free Drink</div>
                            </td>
                            <td class="bg-light-gray">

                            </td>
                            <td>
                                <span class="bg-beige activity-name">Happy Lunch</span>
                                <div class="activity-time">Free Drink</div>
                            </td>
                            <td class="bg-light-gray">

                            </td>
                            <td class="bg-light-gray">

                            </td>
                        </tr>

                        <tr>
                            <td class="align-middle">15:00 - 17:00</td>
                            <td class="bg-light-gray">

                            </td>
                            <td>
                                <span class="bg-earth activity-name">Afternoon Snack</span>
                                <div class="activity-time">Discount 20% Nachos & Dessert </div>
                            </td>
                            <td class="bg-light-gray">

                            </td>
                            <td>
                                <span class="bg-earth activity-name">Afternoon Snack</span>
                                <div class="activity-time">Discount 20% Nachos & Dessert </div>
                            </td>
                            <td class="bg-light-gray">

                            </td>
                            <td class="bg-light-gray">

                            </td>
                            <td class="bg-light-gray">

                            </td>
                        </tr>

                        <tr>
                            <td class="align-middle">18:00 - 20:00</td>
                            <td class="bg-light-gray">

                            </td>
                            <td class="bg-light-gray">

                            </td>
                            <td class="bg-light-gray">

                            </td>
                            <td class="bg-light-gray">

                            </td>
                            <td>
                                <span class="bg-green activity-name">Happy Dinner</span>
                                <div class="activity-time">Discount 15%</div>
                            </td>
                            <td>
                                <span class="bg-green activity-name">Happy Dinner</span>
                                <div class="activity-time">Discount 15%</div>
                            </td>
                            <td>
                                <span class="bg-green activity-name">Happy Dinner</span>
                                <div class="activity-time">Discount 15%</div>
                            </td>
                        </tr>
                    </tbody>
                </table>
            </div>
        </div>

        <div class="promotions-item">
            <div class="content-box">
                <img src="../assets/images/dis-1.jpg" alt="">
            </div>

            <div class="description">
                <h3>party taco upto 50% off</h3>
                <ul>
                    <li>
                        <p>Order more than 10 tacos will get discount 50%</p>
                    </li>
                    <li>
                        <p>Only weekend night</p>
                    </li>
                    <li>
                        <p>Only online payment method</p>
                    </li>
                </ul>
                <router-link @click="scrollToTop()" to="/menu" class="btn">order now</router-link>
            </div>
        </div>

        <div class="promotions-item">
            <div class="content-box">
                <img src="../assets/images/dis-2.png" alt="">
            </div>

            <div class="description">
                <h3>Happy lunch upto 25% extra</h3>
                <ul>
                    <li>
                        <p>Free up size burrito</p>
                    </li>
                    <li>
                        <p>Only lunch from 10am to 2pm</p>
                    </li>
                    <li>
                        <p>Only delivery</p>
                    </li>
                </ul>
                <router-link @click="scrollToTop()" to="/menu" class="btn">order now</router-link>
            </div>
        </div>

        <div class="promotions-item">
            <div class="content-box">
                <img src="../assets/images/dis-3.jpg" alt="">
            </div>

            <div class="description">
                <h3>New drink 100% Cashback</h3>
                <ul>
                    <li>
                        <p>Free 01 Michelada when total bill more than $20</p>
                    </li>
                    <li>
                        <p>From 23/11/2021 to 12/12/2021</p>
                    </li>
                    <li>
                        <p>Only online payment method</p>
                    </li>
                </ul>
                <router-link @click="scrollToTop()" to="/menu" class="btn">order now</router-link>
            </div>
        </div>

    </div>

</template>

<script>
export default {
    name: "Promo",

    methods: {
        scrollToTop() {
            window.scrollTo(0, 0);
        }
    }
}
</script>

<style scoped>
.promotions {
    padding: 2rem 9%;
}

.promotions .promotions-item {
    width: 100%;
    margin-bottom: 20px;
    position: relative;
    display: flex;
}

/* Table */
.activity-name {
    padding: 5px 15px;
    border-radius: 5px;
    margin-bottom: 10px;
    font-size: 14px;
}

.activity-time {
    margin-top: 10px;
    font-size: 12px;
}

.bg-light-gray {
    background-color: #f7f7f7;
}

.table-bordered thead td,
.table-bordered thead th {
    border-bottom-width: 2px;
}

.table thead th {
    vertical-align: bottom;
    border-bottom: 2px solid #dee2e6;
}

.table-bordered td,
.table-bordered th {
    border: 1px solid #dee2e6;
}

.bg-green {
    background-color: #76BA99;
    color: white;
}

.bg-brown {
    background-color: #876445;
    color: white;
}

.bg-beige {
    background-color: #CA955C;
    color: white;
}

.bg-earth {
    background-color: #EDDFB3;
    color: white;
}

.table-bordered td,
.table-bordered th {
    border: 1px solid #dee2e6;
}

.table td,
.table th {
    padding: .75rem;
    vertical-align: top;
    border-top: 1px solid #dee2e6;
}

/* banner */
.promotions .promotions-item .content-box {
    position: relative;
    overflow: hidden;
    border-radius: 1rem;
    height: 30rem;
    max-width: 25%;
}

.promotions .promotions-item .content-box img {
    width: 100%;
    height: 100%;
    object-fit: cover;
}

.promotions .promotions-item .description {
    position: relative;
    margin-left: 50px;
}

.promotions .promotions-item .description h3 {
    font-size: 28px;
    margin-bottom: 20px;
    color: #27ae60;
}

.promotions .promotions-item .description ul {
    margin-left: 40px;
}

.promotions .promotions-item .description li {
    margin-bottom: 30px;
}

.promotions .promotions-item .description p {
    font-size: 20px;
}

.promotions .promotions-item .description .btn {
    position: absolute;
    bottom: 0;
    left: 0;

}

@media (max-width: 768px) {

    .bg-green,
    .bg-brown,
    .bg-beige,
    .bg-earth {
        padding: 5px 0px;
        background-color: inherit;
        text-align: center;
    }

    .bg-green {
        color: #76BA99;
    }

    .bg-brown {
        color: #876445;
    }

    .bg-beige {
        color: #CA955C;
    }

    .bg-earth {
        color: #EDDFB3;
    }

    .promotions .promotions-item .content-box {
        max-width: 30%;
    }

    .promotions .promotions-item .content-box img {
        width: 100%;
        height: 100%;
        object-fit: fill;
    }

    .promotions .promotions-item .description h3 {
        font-size: 22px;
    }

    .promotions .promotions-item .description p {
        font-size: 12px;
    }


}

@media (max-width: 576px) {

    .activity-name {
        font-size: 10px;
    }

    .activity-time {
        font-size: 8px;
    }

    .promotions .promotions-item .content-box {
        position: absolute;
        z-index: -1;
        opacity: 0.8;
        max-width: 100%;
        width: 100%;
        max-height: 100%;
        filter: brightness(40%);
    }

    .promotions .promotions-item .description {
        margin: 0;
        margin-left: 10px;
    }

    .promotions .promotions-item .description h3 {
        font-size: 14px;
        font-weight: 700;
        filter: brightness(150%);
    }

    .promotions .promotions-item .description p {
        font-size: 12px;
        color: white;
        font-weight: 500;
    }

    .promotions .promotions-item .description ul {
        margin-left: 10px;
        margin-bottom: 50px;
        list-style-type: none;
    }

    .promotions .promotions-item .description li {
        margin-bottom: 10px;
    }


}
</style>