Vue.component("dodaj-disk",{
    data:function () {
        return{
            ime: "",
            tip: "",
            kapacitet: "",
            vm: "",
            org: "",
            vmasine: [],
            organizacije: [],
            tipoviDiska:["SSD", "HDD"],
            rule:[v=>!!v||'Ovo polje je obavezno']

        }
    },
    watch:{
        org:function (val) {
            console.log(val);
            
            if(val){
               for(let o of this.organizacije){
                console.log(o.resursi);
                  if(o.id === val){
                    if(o.resursi){
                        
                        this.vmasine = [];
                        for(let r of o.resursi){
                            if(r.tipResursa=="VM"){
                                this.vmasine.push(r);
                            }
                        }
                    }

                    // break;
                   }
               }
            }
        }
    },
    mounted:function () {
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
        dodajDisk:function() {
            if(!this.$refs.forma.validate()){
                return;
            }
            let promise = axios.post("/diskovi",{
                ime: this.ime,
                tipDiska: this.tip,
                kapacitet: this.kapacitet,
                vm:this.vm,
                organizacija:this.org
              }
            )
            promise.then(response=>{
                    
                    if (response.status) {
                        this.$router.push("/disk");
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
    template:`
<div>
    <v-card>
        <v-container>
            <v-row>
                <v-col cols="8">
                    <h2>Novi disk</h2>
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
                    v-model="ime"
                    label="Ime diska"
                    required
                    :rules="rule"
                >
                </v-text-field>
                <v-select
                    required
                    :items="organizacije"
                    solo
                    item-text="ime"
                    item-value="id"
                    :rules="rule"
                    label="Organizacija"
                    v-model="org"
                >
                </v-select>
                <v-select
                    required
                    :items="tipoviDiska"
                    solo
                    :rules="rule"
                    label="Tip diska"
                    v-model="tip"
                >
                </v-select>
                <v-text-field
                    v-model="kapacitet"
                    label="Kapacitet"
                    required
                    :rules="rule"
                    type="number"
                    min="1"
                >
                </v-text-field>
                <v-select
                    :items="vmasine"
                    item-text="ime"
                    item-value="id"
                    label="Virtuelna mašina"
                    v-model="vm"
                >
                </v-select>
            </v-form>
            <button class="btn btn-success" @click = "dodajDisk()">Dodaj novi disk</button>
        </v-container>
    </v-card>
</div>
    `
});




