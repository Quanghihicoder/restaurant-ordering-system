import axios from "axios";

window.axios = axios
axios.defaults.withCredentials = false
// axios.defaults.baseURL = "http://localhost:8000/api"
let backendUrl = "http://" + window.location.hostname.toString() + ":8001/api"
axios.defaults.baseURL = backendUrl
