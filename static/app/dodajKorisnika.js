Vue.component("dodaj-korisnika", {
	data: function () {
        return {
            korisnik : null,
            email: "",
            ime: null,
            prezime: null,
            tipKorisnika: null,
            sifra: null,
            organizacija: null,
            organizacije : [],
            rules:[v=>!!v||'Ovo polje je obavezno'],
            erules:[v=>!!v||'Ovo polje je obavezno', e=>e.match(/^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/)?!!e:"Email ne odgovara obrascu korisnik@kompanija.domen!"],
            tipoviKorisnika:['admin','korisnik']
        }
	},
	methods : {
        dodajKorisnika:function() {
            if(!this.$refs.forma.validate()){
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
                this.$router.push("/korisnik");
            }).catch(error=>{
                new Toast({
                    message:error.response.data.ErrorMessage,
                    type: 'danger'
                });
            });
        },
        back:function () {
            this.$router.push("/korisnik");
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
            console.log(this.korisnik);
            
            this.organizacija = response.data.organizacija;
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
    <h4 class="my-10 error" v-if  = "organizacije.length == 0"  >
        Dodavanje korisnika nije moguće jer ne postoji nijedna organizacija
    </h4>
    <v-card v-else>
        <v-container>
        <v-row>
            <v-col cols="8">
                <h2>Novi korisnik</h2>
            </v-col>
            <v-col>
                <v-btn color="primary" @click="back">Nazad</v-btn>
            </v-col>
        </v-row>
        <v-form ref="forma">
            <v-text-field
            required
            :rules="rules"
            v-model="ime"
            label="Ime korisnika"
            >
            </v-text-field>
            <v-text-field
            required
            :rules="rules"
            v-model="prezime"
            label="Prezime korisnika"
            >
            </v-text-field>
            <v-text-field
            required
            :rules="erules"
            v-model="email"
            label="Email korisnika"
            >
            </v-text-field>
            <v-text-field
                v-model="sifra"
                label="Šifra korisnika"
                type="password"
                required
                :rules="rules"
            >
            </v-text-field>
            <v-select
                label="Tip korisnika"
                :items="tipoviKorisnika"
                required
                :rules="rules"
                v-model="tipKorisnika"
            >
            </v-select>
            <v-select
                label="Organizacija korisnika"
                :items="organizacije"
                required
                :rules="rules"
                item-text="ime"
                item-value="id"
                v-model="organizacija"
                :disabled="korisnik.uloga!='SUPER_ADMIN'?true:false"
            >
            </v-select>
        </v-form>
        <v-btn color="success" v-on:click = "dodajKorisnika()">Dodaj korisnika</v-btn>
        </v-container>
    </v-card>		  
</div>
`
});




