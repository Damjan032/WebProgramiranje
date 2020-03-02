
Vue.component("detalji-kat", {
	data: function () {
        return {
            kategorija : "",
            ime : "",
            brJezgara : 0,
            ram : 0,
            gpuJezgara : 0,
            id:null,

            valid:false,
            rule:[v=>!!v||'Ovo polje je obavezno']
        }
    },
	methods : {
		checkParams: checkFormParams
        ,
        izmenaKat:function(){
            if(!this.$refs.forma.validate()){
                return;
            }
            let promise = axios.put("/vmKategorije/"+this.kategorija.id,{
                ime: this.ime,
                brJezgra: this.brJezgara,
                RAM: this.ram,
                brGPU: this.gpuJezgara,
              }
            )
            promise.then(response=>{
                this.$router.push("/kat");
            }).catch(error=>{
                let msg = error.response.data.ErrorMessage;
                new Toast({
                    message:msg,
                    type: 'danger'
                });
            });
        },obrisi:function(){
            axios.delete('/vmKategorije/'+this.id).then(response => {
                this.$router.push("/kat");
            }).catch(error=>{
                let msg = error.response.data.ErrorMessage;
                new Toast({
                    message:msg,
                    type: 'danger'
                });
            });
        },
        back:function () {
            this.$router.go(-1);
        }
    },
	mounted () {
        
        this.kategorija =  this.$route.params.kat;
        this.id =  this.$route.params.kat.id;
        this.ime =  this.$route.params.kat.ime;
        this.gpuJezgara =  this.$route.params.kat.brGPU;
        this.brJezgara =  this.$route.params.kat.brJezgra;
        this.ram =  this.$route.params.kat.RAM;
        this.tipKorisnika = this.$route.params.tipKorisnika;
        if(this.tipKorisnika!="SUPER_ADMIN"){
            $("select input").prop("readonly", true);
        }
    },
    template: ` 
<div class="container">
    <v-row>
    <v-col cols="10">
            <h3>Detalji kategorije virtuelne mašine</h3>
        </v-col>
        <v-col cols="2">
            <v-container class="d-flex flex-row-reverse">
                <v-btn color="primary" @click="back">Nazad</v-btn>
            </v-container>
        </v-col>
    </v-row>
    <v-card>
        <v-container>
            <h1>Kategorija: {{$route.params.kat.ime}}</h1>
            <v-form ref="forma">
                <v-text-field
                        v-model="ime"
                        label="Naziv kategorije"
                        required
                        :rules="rule"
                    >
                </v-text-field>
                <v-text-field
                    v-model="brJezgara"
                    label="Broj jezgara kategorije"
                    required
                    :rules="rule"
                    type="number"
                    min="1"
                >
                </v-text-field>
                <v-text-field
                    v-model="ram"
                    label="RAM (u GB)"
                    required
                    :rules="rule"
                    type="number"
                    min="1"
                >
                </v-text-field>
                <v-text-field
                    v-model="gpuJezgara"
                    label="GPU jezgra"
                    required
                    :rules="rule"
                    type="number"
                    min="0"
                >
                </v-text-field>
            </v-form>
            <v-btn color="success" @click = "izmenaKat()">Izmeni kategorije</v-btn>
            <v-btn color="error" @click = "obrisi()">Obriši kategoriju</v-btn>
        </v-container>
    </v-card>
</div>		  
`
	
});




