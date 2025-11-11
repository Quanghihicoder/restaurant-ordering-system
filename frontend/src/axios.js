import axios from "axios";

window.axios = axios;
axios.defaults.withCredentials = false;
axios.defaults.baseURL = process.env.VUE_APP_API_BASE_URL;
