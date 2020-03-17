Vue.component("pregled-disk",{
    data:function () {
        return{
            selektDisk:null,
            tipKorisnika:null,
            diskovi:[],
        }
    },
    mounted:function () {
        axios.get("/diskovi").then(response=>{
            this.diskovi = response.data;
        }).catch(error=>{
            new Toast({
                message:error.response.data.ErrorMessage,
                type: 'danger'
            });
        });
        
        axios.get('/korisnik').then(response => {
            if(response.data==null){
                window.location.replace("/");
            }
            this.tipKorisnika = response.data.uloga;
        }).catch(error=>{
            new Toast({
                message:error.response.data.ErrorMessage,
                type: 'danger'
            });
        });
    },
    methods:{
        toDetaljiDisk:function(disk) {
            this.$router.push({name:'detaljiDiska', params:{disk:disk, tipKorisnika:this.tipKorisnika}});
        },
    },
    template:`
<div>
    <v-card>
    <v-container>

        <h2>Pregled diskova</h2>
        
        <h4 class="error my-10" v-if="diskovi&&diskovi.length==0">
        Trenutno nema diskova za pregled.
        </h4>
        <v-simple-table v-else>
            <thead>
                <th>
                    Ime
                </th>
                <th>
                    Kapacitet
                </th>
                <th>
                    Virtuelna mašina
                </th>
            </thead>
            <tbody>
                <tr v-for="disk in diskovi" @click="toDetaljiDisk(disk)">
                    <td>
                        {{disk.ime}}
                    </td>
                    <td>
                        {{disk.kapacitet}}     
                    </td>
                    <td>
                        <template v-if="disk.vm">
                            {{disk.vm.ime}}
                        </template>
                    </td>
                </tr>
            </tbody>
        </v-simple-table>
        <router-link v-if = "tipKorisnika!='KORISNIK'" to="/dodajDisk">
            <v-btn color="success">
                Dodaj disk
            </v-btn>
        </router-link>
        </v-container>
    </v-card>
</div>
    `


});