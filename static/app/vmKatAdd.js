let vmKatAdd = new Vue({
    el:"#vmKatAdd",
    data : {
        ime : "",
        brJezgara : 0,
        ram : 0,
        gpuJezgara : 0
    },
    mounted () {
            let uri = window.location.search.substring(1);
            let params = new URLSearchParams(uri);
            console.log(params.get("id"));
    

        },

    methods : {

        checkParams: function(){
            let inputs = document.getElementsByTagName("input");
            let messages = document.getElementsByClassName("alert");
            let flag = true;
            for(let i = 0;i<inputs.length-1;i++){
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

        addKat:function(){
            if(!this.checkParams()){
                return;
            }
            console.log(this.ime);
            console.log(this.brJezgara);
            console.log(this.gpuJezgara);
            if(this.gpuJezgara==""){
                this.gpuJezgara=0;
            }
            console.log(this.gpuJezgara);
            let promise = axios.post("/vmKategorije",{
                ime: this.ime,
                brJezgra: this.brJezgara,
                RAM: this.ram,
                brGPU: this.gpuJezgara,
              }
            )
            promise.then(response=>{
                window.location.href="/vmKatPregled.html";

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

