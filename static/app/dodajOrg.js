Vue.component("dodaj-org",{
    data:function () {
        return{
            oIme : "",
            oOpis : "",
            slika : "",
            odabranaSlika:null,
            rule:[v=>!!v||'Ovo polje je obavezno']

        }
    },
    mounted:function () {
        axios.get('/virtuelneMasine').then(response => {
            this.vmasine = response.data;
        }).catch(error=>{
            let msg = error.response.data.ErrorMessage;
            new Toast({
                message:msg,
                type: 'danger'
            });
        }); 
        axios.get('/organizacije').then(response => {
            this.organizacije = response.data;
        }).catch(error=>{
            let msg = error.response.data.ErrorMessage;
            new Toast({
                message:msg,
                type: 'danger'
            });
        }); 
    },
    methods:{

        onFileSelected(event){
            this.slika = event.target.files[0];
        },

        addOrganizaciju:function(ime, opis){
            if(!this.$refs.forma.validate()){
                return;
            }
            // console.log(this.slika.name);
            let data = new FormData();
            data.append("nazivSlike", this.odabranaSlika.name)
            data.append('oIme', ime);
            data.append('oOpis', opis);
            data.append('oSlika',this.odabranaSlika);
            let promise = axios.post('/organizacije', data, { headers: {'Content-Type': 'multipart/form-data'}});

            promise.then(response=>{
                    this.$router.push("/org");
            }).catch(error=>{
                let msg = error.response.data.ErrorMessage;
                new Toast({
                    message:msg,
                    type: 'danger'
                });
            });
        },
        back:function () {
            this.$router.go("/org");
        }
    },
    template:`
<div>
    <v-container>
        <v-card>
            <v-container>
                <v-row>
                    <v-col cols="8">
                        <h2>Nova organizacija</h2>
                    </v-col>
                    <v-col>
                        <router-link to="/">
                            <v-btn color="primary" @click="back">Nazad</v-btn>
                        </router-link>
                    </v-col>
                </v-row>
                <v-form
                    ref="forma"    
                >
                    <v-text-field
                        v-model="oIme"
                        label="Ime nove organizacije"
                        required
                        :rules="rule"
                    >
                    </v-text-field>
                    <v-text-field
                        v-model="oOpis"
                        label="Opis nove organizacije"
                        required
                        :rules="rule"
                    >
                    </v-text-field>
                    <v-file-input prepend-icon="mdi-camera" accept="image/*" label="Logo organizacije" v-model="odabranaSlika" required :rules="rule"></v-file-input>
                </v-form>
                <v-form-actions>
                    <v-btn color="success" @click = "addOrganizaciju(oIme, oOpis)">Dodaj organizaciju</v-btn>
                </v-form-actions>
            </v-container>
        </v-card>
    </v-container>
</div>
    `
});




