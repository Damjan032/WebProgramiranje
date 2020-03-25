Vue.component("racuni",{

    data: function () {
        return {
            racuni : null,
            pocetniDatum : null,
            krajnjiDatum : null,
            intervalniRacun:null
        }
    }, 
    mounted () {
       axios.get("/racuni").then(res=>{
           this.racuni = res.data;
       }).catch(error=> {
        let msg = error.response.data.ErrorMessage;
        new Toast({
            message: msg,
            type: 'danger'
        });
    });
        

    },
    methods:{
        pretrazi:function () {
            if(!this.pocetniDatum||!this.krajnjiDatum){
                new Toast({
                    message: 'Niste uneli datume',
                    type: 'danger'
                });
                return;
            }
            axios.get("/racuni/"+this.pocetniDatum+"/"+this.krajnjiDatum)
            .then(response=>{
                this.intervalniRacun = response.data;
            })
            .catch(error=> {
                let msg = error.response.data.ErrorMessage;
                new Toast({
                    message: msg,
                    type: 'danger'
                });
            });
        }
    },
    template:
`
        <div>
            <v-container>
                <h1>Pregled računa</h1>
                <v-row>
                    <v-col>
                        <v-card>
                            <v-container>

                                <h3>
                                    Mesečni računi
                                </h3>
                                <v-simple-table>
                                    <thead class="thead-dark">
                                    <tr>
                                        <th>
                                            Organizacija
                                        </th>
                                        
                                        <th>
                                            Početak
                                        </th>
                                        <th>
                                            Kraj
                                        </th>
                                        <th>
                                            Cena
                                        </th>
                                    </tr>
                                    </thead>
                                    <tbody>
                                        <tr v-for="r in racuni">
                                            <td>
                                                {{r.org.ime}}
                                            </td>
                                            <td>
                                                <template v-if="r.pocetak">

                                                    {{r.pocetak}}
                                                </template>
                                            </td>
                                            <td>
                                                <template v-if="r.kraj">
                                                    {{r.kraj.day}}-{{r.kraj.month}}-{{r.kraj.year}}
                                                </template>
                                            </td>
                                            <td>
                                                {{r.cena.toFixed(2)}}
                                            </td>
                                        </tr>
                                    </tbody>
                                </v-simple-table>
                            </v-container>
                        </v-card>
                    </v-col>
                    <v-col>
                        <v-card>
                            <v-container>
                                <h3>
                                    Cena za određeni period
                                </h3>
                                <v-simple-table>
                                    <tr>
                                        
                                        <th>
                                            Početni datum
                                        </th>
                                        <th>
                                            Krajnji datum
                                        </th>
                                    </tr>
                                    <tr>
                                        <td>
                                            <v-date-picker v-model="pocetniDatum" show-current="2013-07-13"></v-date-picker>

                                        </td>
                                        <td>
                                            <v-date-picker v-model="krajnjiDatum" show-current="2013-07-13"></v-date-picker>
                                        </td>
                                        <td>
                                            <v-btn @click= "pretrazi()" color="success">Pretraži</v-btn>
                                        </td>
                                    </tr>

                                </v-simple-table>
                                <v-simple-table v-if="intervalniRacun">
                                    <thead class="thead-dark">
                                        <tr>
                                            <th>
                                                Ime
                                            </th>
                                            
                                            <th>
                                                Tip
                                            </th>
                                            <th>
                                                Cena za period
                                            </th>
                                        </tr>
                                    </thead>
                                    <tbody>

                                        <tr v-for="racun in intervalniRacun.resursRacuni">
                                            <td>
                                                {{racun.imeResursa}}
                                            </td>
                                            <td>
                                                {{racun.tipResursa}}
                                            </td>
                                            <td>
                                                {{racun.cena.toFixed(2)}}
                                            </td>
                                        </tr>
                                    </tbody>
                                    <thead class="thead-light">
                                        <tr>
                                            <th></th>
                                            <tH>Ukupna cena: </tH>
                                            <th>{{intervalniRacun.ukupnaCena.toFixed(2)}}</th>
                                        </tr>
                                    </thead> 
                                </table>
                            </v-container>
                        </v-card>
                    </v-col>
                </v-row>
            </v-container>
        </div>
 
`
});

