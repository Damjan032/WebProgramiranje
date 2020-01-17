new Vue({
    el:"#izmena-profila",
    data: {
        korisnik : null,
        email:null,
        ime:null,
        prezime:null,
        sifra1:null,
        sifra2:null
    },
    created(){
        axios.get("/korisnik").then((res)=>{
            if(res.status!=200){
                new Toast({
                    message:response.statusText,
                    type: 'danger'
                });
            }else{
                this.korisnik = res.data;
                this.email = this.korisnik.email;
                this.ime = this.korisnik.ime;
                this.prezime = this.korisnik.prezime;
            }
        });
    },
    mounted() {
        // axios.get("/korisnik").then((res)=>{
        //     if(res.status!=200){
        //         new Toast({
        //             message:response.statusText,
        //             type: 'danger'
        //         });
        //     }else{
        //         this.korisnik = res.data;
        //     }
        // });
    },
    methods:{
        izmeniProfil:function () {
            if(this.sifra1&&!this.sifra2){
                new Toast({
                    message:"Niste ponovili šifru",
                    type: 'danger'
                });
            }else if(!this.sifra1&&this.sifra2){
                new Toast({
                    message:"Niste uneli šifru u prvo polje",
                    type: 'danger'
                });
            }else if(this.sifra1&&this.sifra1&&(this.sifra1!==this.sifra2)){
                new Toast({
                    message:"Šifre se ne slažu",
                    type: 'danger'
                });
            }else{
                axios.put("/korisnici",{
                    email: this.email,
                    ime: this.ime,
                    prezime: this.prezime,
                    sifra:this.sifra1
                    }
                ).then(response=>{   
                    if (response.status == 200) {
                        new Toast({
                            message:response.statusText,
                            type: 'success'
                        });
                    }else{
                        new Toast({
                            message:response.statusText,
                            type: 'danger'
                        });
                    }
                }).catch(error=>{
                    new Toast({
                        message:error.response.data.ErrorMessage,
                        type: 'danger'
                    });
                });
            }
       }
    }
});


