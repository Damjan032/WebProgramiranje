let virtuelnaMasinaAdd = new Vue({
    el:"#virtuelnaMasinaAdd",
    data : {
        vmKategorije : "",
        ime : "",
        kategorija : "",
        virtuelnaMasina: ""
    },
    mounted () {
            axios.get('/vmKategorije').then(response => {
                this.vmKategorije = response.data;
                console.log(this.vmKategorije);
            });
            let uri = window.location.search.substring(1);
            let params = new URLSearchParams(uri);
            console.log(params.get("id"));

            axios.get('/virtuelneMasine/'+params.get("id")).then(response => {
                this.virtuelnaMasina = response.data;
                console.log(this.virtuelnaMasina);
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

        izmeni:function(){
            console.log(this.kategorija);
            console.log(this.ime);
            if(!this.checkParams()){
                return;
            }

            let promise = axios.put("/virtuelneMasine/"+this.virtuelnaMasina.id,{
                ime: this.ime,
                kategorija: this.kategorija
              }
            )
            promise.then(response=>{


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

