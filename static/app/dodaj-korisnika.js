let loginapp = new Vue({
    el:"#korisnici",
    data: {
        korisnik : null,
        email: null,
       ime: null,
       prezime: null,
       tipKorisnika: null,
       sifra: null,
       organizacija: null,
       organizacije : null
    },
    mounted : function() {
        axios.get('/organizacije').then(response => {
            this.organizacije = response.data;
        }); 
        axios.get('/korisnik').then(response => {
                this.korisnik = response.data;
                let org = response.data.uloga;
                $("#org").prop("disabled", true).prop("value", org);
            });   
    },
    
    methods:{
        checkParams: checkFormParams
        ,
        dodajKorisnika:function() {
            if(!this.checkParams()){
                return;
            }
            let promise = axios.post("/korisnici",{
                email: this.email,
                ime: this.ime,
                prezime: this.prezime,
                sifra: this.sifra,
                organizacija: this.organizacija,
                uloga:this.tipKorisnika
              }
            )
            promise.then(response=>{
                    
                    if (response.data.status) {
                        window.location.replace("/korisnici.html");
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



