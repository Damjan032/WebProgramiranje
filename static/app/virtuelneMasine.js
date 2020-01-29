Vue.use(VueRouter);
const vm = { template: '<vm></vm>' }
const detaljiVM = { template: '<detalji-vm></detalji-vm>' }
const dodajVM = { template: '<dodaj-vm></dodaj-vm>' }

const router = new VueRouter({
    routes: [
        { path: '/',
            component: vm
        },
        { path: '/dodajVM',
            component: dodajVM
        },
        { path: '/detaljiVM/:disk/:tipKorisnika',
            name:"detaljiVM",
            component: detaljiVM,
            props: true
        }
    ]
});

let vmapp = new Vue({
    router,
    el:"#vmapp",
    data:{
        selVM:null
    },

    methods:{

    }
});