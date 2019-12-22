let loginapp = new Vue({
    el:"#login",
    data: {
        kime : "",
        sifra :""
    },
    
    methods:{
        checkParams: function(){
            let inputs = document.getElementsByTagName("input");
            let messages = document.getElementsByClassName("alert");
            let flag = true;
            for(let i = 0;i<inputs.length;i++){
                let inp = inputs[i];
                let msg = messages[i];
                if (!inp.value) {
                    // inp.classList.add("alert-danger")
                    msg.classList.remove("d-none");
                    flag = false;
                }else{
                    // inp.classList.remove("alert-danger");
                    msg.classList.add("d-none");
                }
            }
            return flag;
        },

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



