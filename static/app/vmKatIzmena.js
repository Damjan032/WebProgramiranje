let vmKatAdd = new Vue({
    el:"#vmKatIzmena",
    data : {
        kategorija : "",
        ime : "",
        brJezgara : 0,
        ram : 0,
        gpuJezgara : 0
    },
    mounted () {
        let uri = window.location.search.substring(1);
        let params = new URLSearchParams(uri);
        console.log(params.get("id"));

        axios.get('/vmKategorije/'+params.get("id")).then(response => {
            this.kategorija = response.data;
            this.ime = this.kategorija.ime;
            this.ram = this.kategorija.RAM;
            this.brJezgara = this.kategorija.brJezgra;
            this.gpuJezgara = this.kategorija.brGPU;
            console.log(this.kategorija);
        }).catch(error=>{
            new Toast({
                message:error.response.data.ErrorMessage,
                type: 'danger'
            });
        });

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

        

    }
});