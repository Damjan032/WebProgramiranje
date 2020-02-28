Vue.component("izmena-profila",{
    data:function name(params) {
        return{
            korisnik : null,
            email:null,
            ime:null,
            prezime:null,
            sifra1:null,
            sifra2:null
        }
    },
    mounted(){
        axios.get("/korisnik").then((res)=>{
            this.korisnik = res.data;
            this.email = this.korisnik.email;
            this.ime = this.korisnik.ime;
            this.prezime = this.korisnik.prezime;
        }).catch(error=>{
            new Toast({
                message:error.response.data.ErrorMessage,
                type: 'danger'
            });
        });
    },
    methods:{
        izmeniProfil:function () {

            checkFormParams();
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
    },
    template:
`
<div id="izmena-profila">
    <div class="jumbotron"><h1>Izmena profila</h1></div>
    
    <div class="container">
        <h2>Korisnik: {{email}}</h2>
        <table class="table">                
            <tr>
                <td>
                    Email 
                </td>
                <td>
                    <input type="text" v-model = "email">
                </td>
            </tr>
            <tr>
                <td>
                    Ime 
                </td>
                <td>
                    <input type="text" v-model = "ime">
                </td>
            </tr>
            <tr>
                <td>
                    Prezime
                </td>
                <td>
                    <input type="text" v-model = "prezime">
                </td>
            </tr>
            <tr>
                <td>
                    Šifra
                </td>
                <td>
                    <input id="sifra1" type="password" v-model = "sifra1">
                </td>
            </tr>
            <tr>
                <td>
                    Ponovite šifru
                </td>
                <td>
                    <input id = "sifra2" type="password" v-model = "sifra2">
                </td>
            </tr>
            <tr>
                <td>
                    <button v-on:click = "izmeniProfil()" type="button" class="btn btn-success">Izmeni profil</button>
                </td>
            </tr>
        </table>   
    </div>

</div>
`
});


