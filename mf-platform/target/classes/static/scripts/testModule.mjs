var localScope = "localScope";

export function testModule() {
    var moduleScope = "moduleScope";

    this.getLocalVar = function() {
        return localScope;
    }

    this.getModuleVar = function() {
        return moduleScope;
    }
}