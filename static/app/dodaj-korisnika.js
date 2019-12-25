let loginapp = new Vue({
    el:"#korisnici",
    data: {
        tip : "",
        email: null,
       ime: null,
       prezime: null,
       tipKorisnika: null,
       sifra: null,
       organizacija: null,
       organizacije : null
    },
    mounted : function() {
        axios.get('/getOrganizacije').then(response => {
            this.organizacije = response.data;
        }); 
        axios.get('/getUserType').then(response => {
            this.tip = response.data;
        });
        if(this.tip == "admin"){
            axios.get('/getUserOrg').then(response => {
                let org;
                org = response.data;
                $("#org").prop("disabled", true).prop("value", org);
                
            });
        }   
    },
    
    methods:{
        checkParams: checkFormParams
        ,

        dodajKorisnika:function() {
            if(!this.checkParams()){
                return;
            }
            let promise = axios.post("/dodajKorisnika",{
                email: this.email,
                ime: this.ime,
                prezime: this.prezime,
                sifra: this.sifra,
                organizacija: this.organizacija
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



