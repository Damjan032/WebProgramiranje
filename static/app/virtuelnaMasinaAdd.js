let virtuelnaMasinaAdd = new Vue({
    el:"#virtuelnaMasinaAdd",
    data : {
        vmKategorije : "",
        ime : "",
        kategorija : "",
        org : null,
        organizacije:null,
        tipKorisnika:null
    },
    mounted () {
        axios.get('/vmKategorije').then(response => {
            this.vmKategorije = response.data;
            console.log(this.vmKategorije);
        }).catch(error=>{
            new Toast({
                message:error.response.data.ErrorMessage,
                type: 'danger'
            });
        });
        axios.get("/korisnik")
        .then(res=>{
            if(res.data){            
                this.tipKorisnika = res.data.uloga;
                if(this.tipKorisnika=="ADMIN"){
                    this.org = res.data.organizacija;
                    $("#orginput").prop("readonly", true);
                }else{
                    axios.get('/organizacije').then(response => {
                        this.organizacije = response.data;
                    }).catch(error=>{
                        new Toast({
                            message:error.response.data.ErrorMessage,
                            type: 'danger'
                        });
                    });
                }
            }
        })
        .catch(error=>{
            new Toast({
                message:error.response.data.ErrorMessage,
                type: 'danger'
            })
        });
        
     },


    methods : {

        checkParams:checkFormParams 
        ,

        addVM:function(){
            console.log(this.kategorija);
            if(!this.checkParams()){
                return;
            }

            let promise = axios.post("/virtuelneMasine",{
                virtuelnaMasina:{
                ime: this.ime,
                kategorija: this.kategorija
                },
                org:this.org
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

