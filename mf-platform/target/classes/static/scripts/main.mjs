import { testModule } from 'testModule.mjs';

// var data = [];

var t = new testModule();
// data.push(localScope);
// data.push(moduleScope);
data.push(t.getLocalVar());
data.push(t.getModuleVar());

dataHolderPlugin;