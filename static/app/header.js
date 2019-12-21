let headerapp = new Vue({
    el:"#header",
    data: {
        loggedin : false
    },
    mounted () {
        axios.get('/isloggedin').then(response => {
            loggedin = response.data;})    
    }
});