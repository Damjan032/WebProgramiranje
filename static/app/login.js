$("#header").load("header.html");
$("#footer").load("footer.html");


let loginapp = new Vue({
    el:"#login",
    data: {
        kime : null,
        sifra :null
    },
    
    methods:{
        login:function(k, s) {
            let promise = axios.get("/login",k, s )
            promise.then(response=>{
                if (response.data.status) {
                    // window.location.replace("vmpregled.html");
                }else{
                    alert(response.data.poruka);
                }

            });
        }
    }
});



