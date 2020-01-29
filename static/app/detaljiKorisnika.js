
Vue.component("detalji-korisnika", {
	data: function () {
        return {
            id:null,
            ime: null,
            prezime: null,
            uloga: null,
            email:null,
            organizacija:null
        }
	}
	, 
	methods : {
		checkParams: checkFormParams
        ,
        izmeniKorisnika:function() {
            console.log(this.korisnik);
            if(!this.checkParams()){
                return;
            }
            let promise = axios.put("/korisnici",{
                id:this.id,
                email: this.email,
                ime: this.ime,
                prezime: this.prezime,
                organizacija: this.organizacija,
                uloga:this.uloga
              }
            )
            promise.then(response=>{
                    
                    if (response.status == 200) {
                        window.location.replace("/korisnici.html");
                    }else{
                        new Toast({
                            message:response.statusText,
                            type: 'danger'
                        });
                    }

            });
            promise.catch(error=>{
                new Toast({
                    message:error.response.data.ErrorMessage,
                    type: 'danger'
                });
            })
        },
        obrisiKorisnika:function() {
            let promise = axios.delete("/korisnici/"+this.id);
            promise.then(response=>{
                    
                    if (response.status == 200) {
                        window.location.replace("/korisnici.html");
                    }else{
                        new Toast({
                            message:response.statusText,
                            type: 'danger'
                        });
                    }

            });
            promise.catch(error=>{
                new Toast({
                    message:error.response.data.ErrorMessage,
                    type: 'danger'
                });
            })
        }
    },
	mounted () {
        this.id =  this.$route.params.korisnik.id;
        this.ime =  this.$route.params.korisnik.ime;
        this.prezime =  this.$route.params.korisnik.prezime;
        this.uloga = this.$route.params.korisnik.uloga;
        this.email = this.$route.params.korisnik.email;
        this.organizacija = this.$route.params.korisnik.organizacija.ime;
        // let email = this.selektovaniKorisnik.email;
        // axios.get('/korisnici/'+email).then(response => {
        //     this.korisnik = response.data;
        //     });
    },
    template: `
<div>
    <div class="row">
        <div class="page-header col-8">
            <h1>Korisnik: {{$route.params.korisnik.email}}</h1>
        </div>
        <div>
            <router-link to="/">
                <button type="button" class="btn btn-primary">Nazad</button>
            </router-link>
        </div>
    </div>
    <table class="table">                
        <tr>
            <td>
                Ime 
            </td>
            <td>
                <input class="required" type="text" v-model = "ime">
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
                <input class="required" type="text" v-model = "prezime">
            </td>
            <td >
                <p  class="alert alert-danger d-none">
                    Ovo polje je obavezno!
                </p>
            </td>
        </tr>
        <tr>
            <td>
                Uloga 
            </td>
            <td>
                <select class="required" name="org" v-model="uloga">
                    <option value="admin">Admin</option>
                    <option value="korisnik">Korisnik</option>
                </select> 
                <td >
                    <p  class="alert alert-danger d-none">
                        Ovo polje je obavezno!
                    </p>
                </td>
            </td>                           
        </tr>
        <tr>
            <td>
                Email 
            </td>
            <td>
                {{$route.params.korisnik.email}}
            </td>
        </tr>
        <tr>
            <td>
                Organizacija 
            </td>
            <td>
                <template v-if="$route.params.korisnik.organizacija!=null">
                    {{$route.params.korisnik.organizacija.ime}}
                </template>
            </td>
        </tr>
        <tr>
            <td>
                <button v-on:click = "izmeniKorisnika()" type="button" class="btn btn-success">Izmeni korisnika</button>
                <button v-on:click = "obrisiKorisnika()" type="button" class="btn btn-danger">Obriši korisnika</button>
            </td>
        </tr>
    </table>    
</div>		  
`
});




