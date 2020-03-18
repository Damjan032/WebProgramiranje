
Vue.component("detalji-korisnika", {
	data: function () {
        return {
            id:null,
            ime: null,
            prezime: null,
            staraUloga:null,
            uloga: null,
            email:null,
            organizacija:null,
            orgId:"",
            rules:[v=>!!v||'Ovo polje je obavezno'],
            erules:[v=>!!v||'Ovo polje je obavezno', e=>e.match(/^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/)?!!e:"Email ne odgovara obrascu korisnik@kompanija.domen!"],
            tipoviKorisnika:['admin','korisnik']

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
                organizacija: this.orgId,
                uloga:this.uloga
              }
            )
            promise.then(response=>{    
                this.$router.push("/korisnik");

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
                this.$router.push("/korisnik");
            });
            promise.catch(error=>{
                new Toast({
                    message:error.response.data.ErrorMessage,
                    type: 'danger'
                });
            })
        },
        back:function () {
            this.$router.push("/korisnik");
        }
    },
	mounted () {
        this.id =  this.$route.params.korisnik.id;
        this.ime =  this.$route.params.korisnik.ime;
        this.prezime =  this.$route.params.korisnik.prezime;
        this.staraUloga = this.$route.params.korisnik.uloga;
        this.uloga = this.$route.params.korisnik.uloga;
        this.email = this.$route.params.korisnik.email;
        this.organizacija = this.$route.params.korisnik.organizacija.ime;
        this.orgId = this.$route.params.korisnik.organizacija.id;
        // let email = this.selektovaniKorisnik.email;
        // axios.get('/korisnici/'+email).then(response => {
        //     this.korisnik = response.data;
        //     });
    },
    template: `
<div>
    <v-row>
        <v-col cols="10">
            <h1>Detalji korisnika</h1>
        </v-col>
        <v-col cols="2">
            <v-btn color="primary" @click="back">Nazad</v-btn>
        </v-col>
    </v-row>
    <v-card> 
        <v-container>
            <v-form ref = "forma">
                <v-text-field
                    required
                    label="Ime"
                    v-model="ime"
                    :rules="rules"
                />
                <v-text-field
                    required
                    label="Prezime"
                    v-model="prezime"
                    :rules="rules"
                />
                <v-text-field
                    readonly
                    label="Uloga"
                    v-model="staraUloga"
                />
                <v-select
                    required
                    label="Nova uloga"
                    :items="tipoviKorisnika"
                    v-model="uloga"
                    :rules="rules"
                />
                <v-text-field
                readonly
                :rules="erules"
                v-model="email"
                label="Email"
                />
                <v-text-field
                v-if="$route.params.korisnik.organizacija!=null"
                readonly
                :rules="rules"
                item-text="ime"
                v-model="organizacija"
                label="Organizacija"
                />
            </v-form>
            <v-btn v-on:click = "izmeniKorisnika()" color="success">Izmeni korisnika</v-btn>
            <v-btn v-on:click = "obrisiKorisnika()" color="error">Obriši korisnika</v-btn>
        </v-container>
    </v-card>   
</div>		  
`
});




