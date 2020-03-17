
Vue.component("detalji-diska", {
	data: function () {
        return {
            id:"",
            ime: "",
            tip: "",
            kapacitet: "",
            vm:"",
            vmActiv:false,
            organizacija:"",
            tipKorisnika:"",
            rule:[v=>!!v||'Ovo polje je obavezno'],
            tipoviDiska : ['SSD', 'HDD']
        }
    },
	methods : {
        izmeniDisk:function() {
            console.log(this.korisnik);
            if(!this.$refs.formaDisk.validate()){
                return;
            }
            let promise = axios.put("/diskovi",{
                id:this.id,
                ime: this.ime,
                tip: this.tip,
                kapacitet: this.kapacitet,
                vm:this.vm
              }
            )
            promise.then(response=>{
                this.$router.push("/disk");
            });
            promise.catch(error=>{
                new Toast({
                    message:error.response.data.ErrorMessage,
                    type: 'danger'
                });
            })
        },
        obrisiDisk:function() {
            let promise = axios.delete("/diskovi/"+this.id);
            promise.then(response=>{
                this.$router.push("/disk");


            });
            promise.catch(error=>{
                new Toast({
                    message:error.response.data.ErrorMessage,
                    type: 'danger'
                });
            })
        },
        activnost:function (vm) {
            let promise = axios.put("/virtuelneMasine/activnost/"+vm,{});
            promise.catch(error=>{
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
        disk = this.$route.params.disk
        this.ime =  this.$route.params.disk.ime;
        this.id = this.$route.params.disk.id;
        this.kapacitet =  this.$route.params.disk.kapacitet;
        this.tip = this.$route.params.disk.tip;
        this.vm = this.$route.params.disk.vm;
        this.organizacija = this.$route.params.disk.organizacija;
        let vm = this.vm;
        if(vm){
            if(vm.aktivnosti.length>0){
                if(vm.aktivnosti[vm.aktivnosti.length-1].zavrsetak==null){
                    this.vmActiv = true;
                }
            }
        }
        this.vmActiv;
        this.vmime = this.$route.params.disk.vmime;
        this.tipKorisnika = this.$route.params.tipKorisnika;
        if(this.tipKorisnika=="KORISNIK"){
            $("select input").prop("readonly", true);
        }
    },
    template: ` 
<div>
    <v-row>
        <v-col cols="10">
            <h1>Detalji diska</h1>
        </v-col>
        <v-col cols="2">
            <v-btn color="primary" @click="back">Nazad</v-btn>
        </v-col>
    </v-row>
    <v-card> 
        <v-container>   
            <h3>Disk {{ime}}</h3>
            <v-form ref="formaDisk">
                <v-text-field
                    required
                    label="Ime diska"
                    v-model="ime"
                    :rules="rule"
                >
                </v-text-field>
                <v-divider class="my-3"></v-divider>
                <v-text-field
                    v-model="kapacitet"
                    label="Kapacitet diska"
                    required
                    :rules="rule"
                    type="number"
                    min="1"
                >
                </v-text-field>
                <v-divider class="my-3"></v-divider>
                <v-select
                    required
                    :items="tipoviDiska"
                    solo
                    :rules="rule"
                    label="Tip diska"
                    v-model="tip"
                    :readonly="tipKorisnika=='KORISNIK'?true:false"
                >
                </v-select>
            </v-form>
        </v-container>
        <v-card-actions>
            <v-btn v-if="tipKorisnika!='KORISNIK'" color="success"  @click = "izmeniDisk()">Izmeni disk</v-btn>
            <v-btn v-if="tipKorisnika=='SUPER_ADMIN'" color="error"  @click = "obrisiDisk()">Obriši disk</v-btn>
        </v-card-actions>
    </v-card>  
</div>		  
`
	
});




