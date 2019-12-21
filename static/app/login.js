$("#header").load("header.html");
$("#footer").load("footer.html");


let loginapp = new Vue({
    el:"#login",
    data: {
        kime : "",
        sifra :""
    },
    
    methods:{
        login:function(k, s) {
            let promise = axios.get("/login",{params: {
                kime:k,
                sifra:s
              }
             }
            )
            promise.then(response=>{
                    new Toast({
                        message:response.data.poruka,
                        type: 'danger'
                    });
                    if (response.data.status) {
                        window.location.replace("/vmpregled.html");
                    }

            });
        }
    }
});



