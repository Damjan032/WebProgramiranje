Vue.component("dodaj-korisnika", {
	data: function () {
        return {
            korisnik : null,
            email: null,
            ime: null,
            prezime: null,
            tipKorisnika: null,
            sifra: null,
            organizacija: null,
            organizacije : null
        }
	},
	methods : {
		checkParams: checkFormParams
        ,
        dodajKorisnika:function() {
            if(!this.checkParams()){
                return;
            }
            axios.post("/korisnici",{
                email: this.email,
                ime: this.ime,
                prezime: this.prezime,
                sifra: this.sifra,
                organizacija: this.organizacija,
                uloga:this.tipKorisnika
              }
            ).then(response=>{
                    
                    if (response.status) {
                        window.location.replace("/korisnici.html");
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
        },
        back:function () {
            this.$router.go(-1);
        }
    },
	mounted () {
        axios.get('/organizacije').then(response => {
            this.organizacije = response.data;
        }).catch(error=>{
            let msg = error.response.data.ErrorMessage;
            new Toast({
                message:msg,
                type: 'danger'
            });
        });  
        axios.get('/korisnik').then(response => {
            this.korisnik = response.data;
            let org = response.data.uloga;
            $("#org").prop("disabled", true).prop("value", org);
        }).catch(error=>{
            let msg = error.response.data.ErrorMessage;
            new Toast({
                message:msg,
                type: 'danger'
            });
        });
    },
    template: `
<div>
    <div v-if  = "organizacije.length == 0"  >
        <h2>Dodavanje korisnika nije moguće jer ne postoji nijedna organizacija</h2>
    </div>
    <template v-else>
        <div class="row">
            <div class="page-header col-8">
                <h2>Novi korisnik</h2>
            </div>
            <div>
                    <button type="button" class="btn btn-primary" @click="back">Nazad</button>
            </div>
        </div>
        <p>
            <table>
                <tr>
                    <td>
                        Ime
                    </td>
                    <td>
                        <input class="required" type="text" v-model="ime"/>
                    </td>
                    <td >
                        <p  class="alert alert-danger d-none">
                            Ovo polje je obavezno!
                        </p>
                       
                    </td>
                </tr>
                <tr>
                    <td>
                        Prezime
                    </td>
                    <td>
                        <input class="required" type="text" v-model="prezime"/>
                    </td>
                    <td >
                        <p  class="alert alert-danger d-none">
                            Ovo polje je obavezno!
                        </p>
                    </td>
                </tr>
                <tr>
                    <td>
                        Email
                    </td>
                    <td>
                        <input class="required email"  type="text" v-model="email"/>
                    </td>
        
                    <td>
                        <p  class="alert alert-danger d-none">
                            Ovo polje je obavezno!
                        </p>
                        <p  class="alert email-message alert-danger d-none">
                            Email nije odgovarajućeg formata ime@domen.org
                        </p>
                    </td>
                </tr>
                <tr>
                    <td>
                        Šifra
                    </td>
                    <td>
                        <input class="required" type="password" v-model="sifra"/>
                    </td>
                    <td>
                        <p  class="alert alert-danger d-none">
                            Ovo polje je obavezno!
                        </p>
                    </td>
                </tr>
                <tr>
                    <td>
                        Tip korisnika
                    </td>
                    <td>
                        
                        <select class="required" name="org" v-model="tipKorisnika">
                            <option value="admin">Admin</option>
                            <option value="korisnik">Korisnik</option>
                        </select class="required">
                    </td>
                    <td >
                        <p  class="alert alert-danger d-none">
                            Ovo polje je obavezno!
                        </p>
                    </td>
                </tr>
                <tr>
                    <td>
                        Organizacija
                    </td>
                    <td>
                        <select class="required" name="org" v-model="organizacija">
                            <option v-for = "o in organizacije" :value="o.id">{{o.ime}}</option>
                        </select class="required">
                    </td>
                    <td >
                        <p  class="alert alert-danger d-none">
                            Ovo polje je obavezno!
                        </p>
                    </td>
                </tr>
                <tr>
                    <td>
                    
                    </td>
                    <td>
                        <button type="button" class="btn btn-success" v-on:click = "dodajKorisnika()">Dodaj korisnika</button>
                    </td>
                </tr> 
            </table>

            
            
        </p>
    </template>		  
</div>
`
});




