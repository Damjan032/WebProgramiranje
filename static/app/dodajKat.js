Vue.component("dodaj-kat",{
    data:function () {
        return{
            ime : null,
            brJezgara : null,
            ram : null,
            gpuJezgara : null,
            rule:[v=>!!v||'Ovo polje je obavezno']

        }
    },
    mounted:function () {
    
    },
    methods:{
        addKat:function(){
            if(!this.$refs.forma.validate()){
                return;
            }
            console.log(this.ime);
            console.log(this.brJezgara);
            console.log(this.gpuJezgara);
            if(this.gpuJezgara==""){
                this.gpuJezgara=0;
            }
            console.log(this.gpuJezgara);
            let promise = axios.post("/vmKategorije",{
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
        },
        back:function () {
            this.$router.go(-1);
        }
    },
    template:`
<div>
    <v-row>
        <v-col cols="8">
            <h2>Dodavanje kategorije</h2>
        </v-col>
        <v-col>
            <router-link to="/">
                <v-btn color="primary" @click="back">Nazad</v-btn>
            </router-link>
        </v-col>
    </v-row>
    <v-container>
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
                min="1"
                type="number"
                label="Broj jezgara"
                required
                :rules="rule"
            >
            </v-text-field>
            <v-text-field
                v-model="ram"
                min="1"
                type="number"
                label="RAM (u GB)"
                required
                :rules="rule"
            >
            </v-text-field>
            <v-text-field
                v-model="gpuJezgara"
                min="0"
                type="number"
                label="GPU jezgra"
                required
                :rules="rule"
            >
            </v-text-field>
        </v-form>
        <v-btn color="success"  @click = "addKat()">Dodaj kategoriju</v-btn>
    </v-container>
</div>
    `
});







