<template>
    <div class="thank-container">
        <h1>
            <p>
                <span>t</span>
                <span>h</span>
                <span>a</span>
                <span>n</span>
                <span>k</span>
            </p>

            <p>
                <span>y</span>
                <span>o</span>
                <span>u</span>
            </p>
        </h1>

        <div class="thank-letter">
            <p v-none>Thank you for giving us your trust!</p>
            <p v-none>
                We have just confirmed you received your order, and hope you are enjoying it.
                Every
                item is handmade by our team, with care to the details, so we can always provide you with the best
                experience.
            </p>
            <router-link class="btn" to="/menu">Continue Shopping</router-link>
        </div>
    </div>
</template>


<script>
export default {
    name: 'Thank',
}
</script>
<script setup>
// enables v-focus in templates
const vNone = {
    mounted(el) {
        el.style.textTransform = "none";
    }
}
</script>

<style scoped>
@import url("https://fonts.googleapis.com/css?family=Luckiest+Guy");

.thank-container {
    padding: 2rem 9%;
    background: #fff;
    height: 90vh;
}


.thank-container h1 {
    width: 100%;
    height: 100px;
    margin: auto;
    display: block;
    text-align: center;
    margin-top: 50px;
}

.thank-container h1 p span {
    position: relative;
    color: #27ae60 !important;
    top: 20px;
    display: inline-block;
    -webkit-animation: bounce 0.3s ease infinite alternate;
    font-size: 80px;
    color: #fff;
    text-shadow: 0 1px 0 #20884b, 0 2px 0 #20884b, 0 3px 0 #20884b, 0 4px 0 #20884b,
        0 5px 0 #20884b, 0 6px 0 transparent, 0 7px 0 transparent, 0 8px 0 transparent,
        0 9px 0 transparent, 0 10px 10px rgba(0, 0, 0, 0.4);
}

.thank-container h1 p span:nth-child(2) {
    -webkit-animation-delay: 0.1s;
}

.thank-container h1 p span:nth-child(3) {
    -webkit-animation-delay: 0.2s;
}

.thank-container h1 p span:nth-child(4) {
    -webkit-animation-delay: 0.3s;
}

.thank-container h1 p span:nth-child(5) {
    -webkit-animation-delay: 0.4s;
}

.thank-letter {
    position: relative;
    top: 180px;
    font-size: 20px;
    width: 40vw;
    margin: 0 auto;
    text-align: center;
}

.thank-letter a {
    margin-top: 20px;
}

@-webkit-keyframes bounce {
    100% {
        top: -20px;
        text-shadow: 0 1px 0 #20884b, 0 2px 0 #20884b, 0 3px 0 #20884b, 0 4px 0 #20884b,
            0 5px 0 #20884b, 0 6px 0 #20884b, 0 7px 0 #20884b, 0 8px 0 #20884b, 0 9px 0 #20884b,
            0 50px 25px rgba(0, 0, 0, 0.2);
    }
}

@media (max-width: 1024px) {
    .thank-container {
        padding: 0px;
    }

    .thank-container h1 {
        margin-top: 20px;
    }

    .thank-container h1 p span {
        font-size: 70px;
    }

    .thank-letter {
        font-size: 16px;
        width: 50vw;
        top: 160px;
    }
}

@media (max-width: 576px) {

    .thank-container h1 {
        margin-top: 50px;
    }

    .thank-container h1 p span {
        font-size: 50px;
    }

    .thank-letter {
        font-size: 14px;
        width: 90vw;
        top: 90px;
    }
}
</style>