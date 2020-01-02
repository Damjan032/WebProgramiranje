
Vue.component("detalji-korisnika", {
	data: function () {
        return {
            ime: null,
            prezime: null,
            uloga: null		
        }
	},
    template: ` 
<div>

    <h1>Korisnik: {{$route.params.korisnik.email}}</h1>
    <table class="table">                
        <tr>
            <td>
                Ime 
            </td>
            <td>
                <input type="text" v-model = "prezime">
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
                <input type="text" v-model = "prezime">
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
                <select name="org" v-model="uloga">
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
    </table>    
    <button v-on:click = "izmeniKorisnika()" type="button" class="btn btn-success">Izmeni korisnika</button>
</div>		  
`
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
                email: this.email,
                ime: this.ime,
                prezime: this.prezime,
                organizacija: this.korisnik.organizacija,
                uloga:this.tipKorisnika
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
        }
    },
    created(){
        console.log(this.korisnik);
        // bus.$emit('kreiran', true);
        // bus.$on('selektovaniKorisnik', (korisnik)=>{
        //     console.log(korisnik);
        //     this.selektovaniKorisnik = korisnik;
        // });
    },
	mounted () {
        console.log(this.korisnik);
        // let email = this.selektovaniKorisnik.email;
        // axios.get('/korisnici/'+email).then(response => {
        //     this.korisnik = response.data;
        //     });
    },
});




