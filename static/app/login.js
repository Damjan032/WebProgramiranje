let loginapp = new Vue({
    el:"#login",
    data: {
        kime : "",
        sifra :""
    },
    
    methods:{
        checkParams: checkFormParams
        ,

        login:function(k, s) {
            if(!this.checkParams()){
                return;
            }
            let promise = axios.get("/login",{params: {
                kime:k,
                sifra:s
              }
             }
            )
            promise.then(response=>{
                   
                    if (response.data.status) {
                        window.location.replace("/virtuelnaMasinaPregled.html");
                    }else{
                        new Toast({
                            message:response.data.poruka,
                            type: 'danger'
                        });
                    }

            });
        }
    }
});



