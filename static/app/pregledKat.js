Vue.component("pregled-kat",{
    data:function () {
        return{
            kategorije : [],
            korisnikType:""
        }
    },
    mounted:function () {
        axios.get('/vmKategorije').then(response => {
            this.kategorije = response.data;
            console.log(this.kategorije);
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
            this.korisnikType = response.data.uloga
        }).catch(error=>{
            new Toast({
                message:error.response.data.ErrorMessage,
                type: 'danger'
            });
        });
    },
    methods:{
        toDetaljiKat:function (kat) {
            this.$router.push(
                {
                    name:'detaljiKat',
                    params:{kat:kat, tipKorisnika:this.korisnikType}
                }
            );

        }
    },
    template:`
<div>
    <v-card>
    <h2>Pregled kategorija</h2>
    <p>
    <h4 class="error my-10" v-if="!kategorije && kategorije.length == 0">
        Trenutno nema kategorija
    </h4>
    <v-simple-table v-else>
        <thead>
            <th>
                Ime
            </th>
            <th>
                Broj jezgara
            </th>
            <th>
                RAM (u GB)
            </th>
            <th>
                Broj GPU jezgara
            </th>
        </thead>
        <tbody>
            <tr v-for = "kat in kategorije" @click="toDetaljiKat(kat)">
                <td text-align="center">
                        <p>{{kat.ime}}</p>
                </td>
                <td text-align="center">
                        {{kat.brJezgra}}
                </td>
                <td text-align="center">
                        {{kat.RAM}}
                </td>
                <td text-align="center">
                        {{kat.brGPU}}
                </td>
            </tr>
        </tbody>
        
            
    </v-simple-table>
    <router-link v-if = "korisnikType=='SUPER_ADMIN'" to="/dodajKat">
        <v-btn color="success">
            Dodaj kategoriju virtuelne mašine
        </v-btn>
    </router-link>
    </p>
    </v-card>
</div>

    `


});