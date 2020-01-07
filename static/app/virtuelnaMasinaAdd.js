let virtuelnaMasinaAdd = new Vue({
    el:"#virtuelnaMasinaAdd",
    data : {
        vmKategorije : "",
        ime : "",
        kategorija : "",
    },
    mounted () {
            axios.get('/vmKategorije').then(response => {
                this.vmKategorije = response.data;
                console.log(this.vmKategorije);
            });
     },


    methods : {

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

        addVM:function(){
            console.log(this.kategorija);
            if(!this.checkParams()){
                return;
            }

            let promise = axios.post("/virtuelneMasine",{
                ime: this.ime,
                kategorija: this.kategorija
              }
            )
            promise.then(response=>{
                window.location.href="/virtuelnaMasinaPregled.html";

            }).catch(error=>{
                let msg = error.response.data.ErrorMessage;
                new Toast({
                    message:msg,
                    type: 'danger'
                });
            });
        }

    }
});

