
Vue.component("detalji-org", {
    data: function () {
        return {
            organizacija:"",
            oId:"",
            oIme : "",
            oOpis : "",
            slika : null,
            rule:[v=>!!v||'Ovo polje je obavezno'],
            tipKorisnika:""
        }
    },
	methods : {
		checkParams: checkFormParams
        ,
        onFileSelected(event){
            this.slika = event.target.files[0];
        },

        izmenaOrg:function(imeOrg, opis){
            if(!this.checkParams()){
                return;
            }
            // console.log(this.slika.name);
            let data = new FormData();
            if(this.slika){
                data.append("nazivSlike", this.slika.name)
            }
            data.append('oIme', imeOrg);
            data.append('oOpis', opis);
            data.append('oSlika',this.slika);
            let promise = axios.put('/organizacije/'+this.organizacija.id, data, { headers: {'Content-Type': 'multipart/form-data'}});

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
        obrisiOrg:function(){
            axios.delete('/organizacije/'+this.oId).then(response => {
                this.$router.push("/org");
            }).catch(error=>{
                new Toast({
                    message:error.response.data.ErrorMessage,
                    type: 'danger'
                });
            });
        },
        back:function () {
            this.$router.push("/org");
        }
    },
	mounted () {
        this.organizacija = this.$route.params.org;
        this.oIme =  this.$route.params.org.ime;
        this.oOpis =  this.$route.params.org.opis;
        this.slika =  this.$route.params.org.slika;
        this.oId =  this.$route.params.org.id;
        this.tipKorisnika = this.$route.params.tipKorisnika;
        if(this.tipKorisnika=="KORISNIK"){
            $("select input").prop("readonly", true);
        }
    },
    template: ` 
<div class="container">
    <v-row>
        <v-col cols="10">
            <h1>Detalji organizacije</h1>
        </v-col>
        <v-col cols="2">
            <v-btn color="primary" @click="back">Nazad</v-btn>
        </v-col>
    </v-row>
    <v-row>
        <v-col cols="12" lg="6">
            <v-card>
                <v-container>

                    
                    <h2><img width="50" height="50" v-bind:src="organizacija.imgPath" class="rounded mr-5">Organizacija : {{organizacija.ime}}</h2>
                        
                    <v-text-field
                        required
                        label="Naziv organizacije"
                        v-model="oIme"
                        :rules="rule"
                        :readonly="tipKorisnika!='SUPER_ADMIN'"
                    >
                    </v-text-field>
                        <v-text-field
                        required
                        label="Opis organizacije"
                        v-model="oOpis"
                        :rules="rule"
                        :readonly="tipKorisnika!='SUPER_ADMIN'"
                    >
                    </v-text-field>
                    <v-file-input v-if="tipKorisnika!='KORISNIK'" prepend-icon="mdi-camera" accept="image/*" label="Novi logo organizacije" v-model="slika"></v-file-input>
                    <v-btn v-if="tipKorisnika!='KORISNIK'" v-on:click = "izmenaOrg(oIme,oOpis)" color="success">Izmeni organizaciju</v-btn>
                    <v-btn v-if="tipKorisnika=='SUPER_ADMIN'" v-on:click = "obrisiOrg()" type="button" color="error">Obriši organizaciju</v-btn>
                </v-container>
            </v-card>
        </v-col> 
        <v-col cols="12" lg="6">
            <v-card>
                <v-container>
                    <h3>
                        Resursi
                    </h3>
                    <h4 v-if="organizacija.resursi.length==0">
                        Trenutno nema resursa u ovoj organizaciji
                    </h4>
                    <table class="table" v-else>                
                        <tr>
                            <th>
                                Ime
                            </th>
                            <th>
                                Tip
                            </th>
                        </tr>
                        <tr v-for="resurs in organizacija.resursi">
                            <td>
                                {{resurs.ime}}
                            </td>
                            <td>
                                {{resurs.tipResursa}}
                            </td>
                        </tr>
                    </table>
                </v-container> 
            </v-card>
        </v-col>
    </v-row>
</div>		  
`
	
});




