
let orgIzmena = new Vue({
    el:"#organizacije",
    data: {
        organizacija : "",
         oIme : "",
         oOpis : "",
         slika : ""
    },
    mounted () {
        let uri = window.location.search.substring(1);
        let params = new URLSearchParams(uri);
        console.log(params.get("id"));

        axios.get('/organizacije/'+params.get("id")).then(response => {
            this.organizacija = response.data;
            this.oIme = this.organizacija.ime;
            this.oOpis = this.organizacija.opis;
            console.log(this.organizacija);
        });

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

        onFileSelected(event){
                    this.slika = event.target.files[0];
               },

        izmenaOrg:function(imeOrg, opis){
            if(!this.checkParams()){
                return;
            }
            // console.log(this.slika.name);
            let data = new FormData();
            data.append("nazivSlike", this.slika.name)
            data.append('oIme', imeOrg);
            data.append('oOpis', opis);
            data.append('oSlika',this.slika);
            let promise = axios.put('/organizacije/'+this.organizacija.id, data, { headers: {'Content-Type': 'multipart/form-data'}});

            promise.then(response=>{
                if (response.data.status) {
                    window.location.replace("/orgPregled.html");
                }

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